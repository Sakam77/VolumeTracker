package com.volumetracker.app.domain.util

import com.volumetracker.app.data.api.dto.RankItem
import com.volumetracker.app.data.db.entity.VolumeSnapshotEntity
import timber.log.Timber
import javax.inject.Inject

class WashTradingDetector @Inject constructor() {
    
    /**
     * Analyzes trading patterns to detect wash trading
     * @return true if volume appears legitimate, false if suspicious
     */
    fun isRealVolume(
        rankItem: RankItem,
        volumeSnapshots: List<VolumeSnapshotEntity> = emptyList()
    ): Boolean {
        val suspicionScore = calculateSuspicionScore(rankItem, volumeSnapshots)
        Timber.d("Wash trading suspicion score for ${rankItem.symbol}: $suspicionScore")
        return suspicionScore < SUSPICION_THRESHOLD
    }
    
    private fun calculateSuspicionScore(
        rankItem: RankItem,
        volumeSnapshots: List<VolumeSnapshotEntity>
    ): Double {
        var score = 0.0
        
        // Check unique wallet count relative to trade count
        val swaps = rankItem.swaps ?: 0
        val uniqueWallets = rankItem.uniqueWallet ?: 0
        
        if (swaps > 0 && uniqueWallets > 0) {
            val walletPerSwapRatio = uniqueWallets.toDouble() / swaps
            // Suspicious if very few wallets do many trades
            if (walletPerSwapRatio < 0.3) {
                score += 30.0
                Timber.d("Low wallet/swap ratio: $walletPerSwapRatio")
            }
        } else if (swaps > 10 && uniqueWallets == 0) {
            // No unique wallet data but many swaps is suspicious
            score += 40.0
        }
        
        // Check buy/sell balance
        val buyCount = rankItem.buyCount ?: 0
        val sellCount = rankItem.sellCount ?: 0
        
        if (buyCount > 0 && sellCount > 0) {
            val buySellRatio = buyCount.toDouble() / (buyCount + sellCount)
            // Extreme imbalance is suspicious (>80% or <20% buys)
            if (buySellRatio > 0.8 || buySellRatio < 0.2) {
                score += 25.0
                Timber.d("Extreme buy/sell ratio: $buySellRatio")
            }
        }
        
        // Check volume concentration over time
        if (volumeSnapshots.size >= 3) {
            val recentVolume = volumeSnapshots.take(3).sumOf { it.volume }
            val totalVolume = volumeSnapshots.sumOf { it.volume }
            
            if (totalVolume > 0) {
                val concentrationRatio = recentVolume / totalVolume
                // If >70% of volume happened in last 3 snapshots, it's suspicious
                if (concentrationRatio > 0.7 && volumeSnapshots.size > 5) {
                    score += 20.0
                    Timber.d("High volume concentration: $concentrationRatio")
                }
            }
        }
        
        return score
    }
    
    companion object {
        private const val SUSPICION_THRESHOLD = 50.0 // Score above this indicates wash trading
    }
}
