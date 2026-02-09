package com.volumetracker.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_history")
data class AlertHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tokenAddress: String,
    val tokenName: String,
    val tokenSymbol: String,
    val tokenLogo: String?,
    val priceAtAlert: Double,
    val currentPrice: Double,
    val volumeMultiplier: Double,
    val marketCap: Double?,
    val timestamp: Long
)

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val tokenAddress: String,
    val tokenName: String,
    val tokenSymbol: String,
    val tokenLogo: String?,
    val addedTimestamp: Long
)

@Entity(tableName = "custom_alert")
data class CustomAlertEntity(
    @PrimaryKey
    val tokenAddress: String,
    val tokenName: String,
    val tokenSymbol: String,
    val volumeThreshold: Double, // e.g., 3.0 for 3x
    val enabled: Boolean = true,
    val createdTimestamp: Long
)

@Entity(tableName = "volume_snapshot")
data class VolumeSnapshotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tokenAddress: String,
    val volume: Double,
    val timestamp: Long,
    val uniqueWallets: Int?,
    val buyCount: Int?,
    val sellCount: Int?
)
