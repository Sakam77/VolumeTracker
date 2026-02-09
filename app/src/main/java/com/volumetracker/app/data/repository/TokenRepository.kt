package com.volumetracker.app.data.repository

import com.volumetracker.app.data.api.GmgnApiService
import com.volumetracker.app.data.api.dto.TokenData
import com.volumetracker.app.data.db.dao.VolumeSnapshotDao
import com.volumetracker.app.data.db.entity.VolumeSnapshotEntity
import com.volumetracker.app.domain.model.PriceDataPoint
import com.volumetracker.app.domain.model.Result
import com.volumetracker.app.domain.model.Token
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val api: GmgnApiService,
    private val volumeSnapshotDao: VolumeSnapshotDao
) {
    
    suspend fun getToken(address: String): Result<Token> {
        return try {
            val response = api.getToken(address)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.Success(response.body()!!.data!!.toDomain())
            } else {
                Result.Error("Token not found: ${response.code()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching token")
            Result.Error("Network error: ${e.message}", e)
        }
    }
    
    suspend fun getTopTokens(timeframe: String = "1h", limit: Int = 50): Result<List<Token>> {
        return try {
            val response = api.getRankBySwaps(timeframe, limit = limit)
            if (response.isSuccessful && response.body()?.data?.rank != null) {
                val tokens = response.body()!!.data!!.rank!!.mapNotNull { rankItem ->
                    try {
                        Token(
                            address = rankItem.address,
                            name = rankItem.name ?: "Unknown",
                            symbol = rankItem.symbol ?: "???",
                            logo = rankItem.logo,
                            price = rankItem.price ?: 0.0,
                            marketCap = rankItem.marketCap ?: 0.0,
                            volume24h = rankItem.volume ?: 0.0,
                            liquidity = 0.0,
                            holderCount = 0,
                            buyVolume = 0.0,
                            sellVolume = 0.0,
                            createdTimestamp = null
                        )
                    } catch (e: Exception) {
                        Timber.w(e, "Error parsing rank item")
                        null
                    }
                }
                Result.Success(tokens)
            } else {
                Result.Error("Failed to fetch rankings: ${response.code()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching top tokens")
            Result.Error("Network error: ${e.message}", e)
        }
    }
    
    suspend fun getPriceHistory(
        address: String,
        period: String = "1h"
    ): Result<List<PriceDataPoint>> {
        return try {
            val response = api.getPriceHistory(address, period)
            if (response.isSuccessful && response.body()?.data?.history != null) {
                val points = response.body()!!.data!!.history!!.map {
                    PriceDataPoint(it.timestamp, it.price, it.volume ?: 0.0)
                }
                Result.Success(points)
            } else {
                Result.Error("Failed to fetch price history: ${response.code()}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching price history")
            Result.Error("Network error: ${e.message}", e)
        }
    }
    
    suspend fun saveVolumeSnapshot(snapshot: VolumeSnapshotEntity) {
        volumeSnapshotDao.insert(snapshot)
    }
    
    suspend fun getVolumeHistory(address: String, since: Long): List<VolumeSnapshotEntity> {
        return volumeSnapshotDao.getForTokenSince(address, since)
    }
    
    suspend fun getRecentVolumeSnapshots(address: String, limit: Int = 30): List<VolumeSnapshotEntity> {
        return volumeSnapshotDao.getLatestForToken(address, limit)
    }
    
    private fun TokenData.toDomain(): Token = Token(
        address = address,
        name = name ?: "Unknown",
        symbol = symbol ?: "???",
        logo = logo,
        price = price ?: 0.0,
        marketCap = marketCap ?: 0.0,
        volume24h = volume24h ?: 0.0,
        liquidity = liquidity ?: 0.0,
        holderCount = holderCount ?: 0,
        buyVolume = buyVolume ?: 0.0,
        sellVolume = sellVolume ?: 0.0,
        createdTimestamp = createdTimestamp
    )
}
