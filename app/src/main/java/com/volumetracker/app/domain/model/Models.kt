package com.volumetracker.app.domain.model

data class Token(
    val address: String,
    val name: String,
    val symbol: String,
    val logo: String?,
    val price: Double,
    val marketCap: Double,
    val volume24h: Double,
    val liquidity: Double,
    val holderCount: Int,
    val buyVolume: Double,
    val sellVolume: Double,
    val createdTimestamp: Long?
)

data class AlertItem(
    val id: Long,
    val token: Token,
    val priceAtAlert: Double,
    val currentPrice: Double,
    val volumeMultiplier: Double,
    val timestamp: Long
) {
    val gainMultiplier: Double
        get() = if (priceAtAlert > 0) currentPrice / priceAtAlert else 0.0
    
    val isProfitable: Boolean
        get() = currentPrice > priceAtAlert
}

data class VolumeSnapshot(
    val tokenAddress: String,
    val volume: Double,
    val timestamp: Long,
    val uniqueWallets: Int,
    val buyCount: Int,
    val sellCount: Int
)

data class CustomAlert(
    val tokenAddress: String,
    val tokenName: String,
    val tokenSymbol: String,
    val volumeThreshold: Double,
    val enabled: Boolean
)

data class PriceDataPoint(
    val timestamp: Long,
    val price: Double,
    val volume: Double
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
