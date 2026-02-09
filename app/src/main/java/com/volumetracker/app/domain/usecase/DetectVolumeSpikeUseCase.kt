package com.volumetracker.app.domain.usecase

import com.volumetracker.app.data.api.GmgnApiService
import com.volumetracker.app.data.db.entity.VolumeSnapshotEntity
import com.volumetracker.app.data.repository.AlertRepository
import com.volumetracker.app.data.repository.TokenRepository
import com.volumetracker.app.domain.model.Token
import com.volumetracker.app.domain.util.WashTradingDetector
import timber.log.Timber
import javax.inject.Inject

class DetectVolumeSpikeUseCase @Inject constructor(
    private val api: GmgnApiService,
    private val tokenRepository: TokenRepository,
    private val alertRepository: AlertRepository,
    private val washTradingDetector: WashTradingDetector
) {
    
    suspend fun detectSpikes(timeframe: String = "1h"): List<Token> {
        val detectedSpikes = mutableListOf<Token>()
        
        try {
            // Fetch top tokens by swap count
            val response = api.getRankBySwaps(timeframe, limit = 50)
            
            if (!response.isSuccessful || response.body()?.data?.rank == null) {
                Timber.w("Failed to fetch rankings: ${response.code()}")
                return emptyList()
            }
            
            val rankItems = response.body()!!.data!!.rank!!
            
            for (rankItem in rankItems) {
                try {
                    val currentVolume = rankItem.volume ?: 0.0
                    if (currentVolume <= 0) continue
                    
                    // Get historical volume data
                    val cutoffTime = System.currentTimeMillis() - (60 * 60 * 1000) // 1 hour ago
                    val volumeHistory = tokenRepository.getVolumeHistory(rankItem.address, cutoffTime)
                    
                    // Calculate average volume
                    val avgVolume = if (volumeHistory.isNotEmpty()) {
                        volumeHistory.map { it.volume }.average()
                    } else {
                        // If no history, assume current volume is the baseline (skip for now)
                        continue
                    }
                    
                    val volumeMultiplier = if (avgVolume > 0) currentVolume / avgVolume else 0.0
                    
                    // Check if it's a 2x spike
                    if (volumeMultiplier >= 2.0) {
                        // Check for wash trading
                        if (washTradingDetector.isRealVolume(rankItem, volumeHistory)) {
                            // Check if we already alerted for this token recently (within 30 minutes)
                            val lastAlert = alertRepository.getLatestAlertForToken(rankItem.address)
                            val timeSinceLastAlert = if (lastAlert != null) {
                                System.currentTimeMillis() - lastAlert.timestamp
                            } else {
                                Long.MAX_VALUE
                            }
                            
                            // Only alert if no recent alert (30 min cooldown)
                            if (timeSinceLastAlert > 30 * 60 * 1000) {
                                val token = Token(
                                    address = rankItem.address,
                                    name = rankItem.name ?: "Unknown",
                                    symbol = rankItem.symbol ?: "???",
                                    logo = rankItem.logo,
                                    price = rankItem.price ?: 0.0,
                                    marketCap = rankItem.marketCap ?: 0.0,
                                    volume24h = currentVolume,
                                    liquidity = 0.0,
                                    holderCount = 0,
                                    buyVolume = 0.0,
                                    sellVolume = 0.0,
                                    createdTimestamp = null
                                )
                                
                                // Save the alert
                                alertRepository.saveAlert(token, token.price, volumeMultiplier)
                                detectedSpikes.add(token)
                                
                                Timber.i("Volume spike detected: ${token.symbol} - ${volumeMultiplier}x")
                            }
                        } else {
                            Timber.d("Wash trading detected for ${rankItem.symbol}, skipping alert")
                        }
                    }
                    
                    // Save current volume snapshot for future calculations
                    val snapshot = VolumeSnapshotEntity(
                        tokenAddress = rankItem.address,
                        volume = currentVolume,
                        timestamp = System.currentTimeMillis(),
                        uniqueWallets = rankItem.uniqueWallet,
                        buyCount = rankItem.buyCount,
                        sellCount = rankItem.sellCount
                    )
                    tokenRepository.saveVolumeSnapshot(snapshot)
                    
                } catch (e: Exception) {
                    Timber.e(e, "Error processing token ${rankItem.address}")
                }
            }
            
        } catch (e: Exception) {
            Timber.e(e, "Error detecting volume spikes")
        }
        
        return detectedSpikes
    }
    
    suspend fun checkCustomAlerts(): List<Token> {
        // TODO: Implement custom alert checking
        return emptyList()
    }
}
