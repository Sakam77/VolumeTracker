package com.volumetracker.app.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @Json(name = "data")
    val data: TokenData?
)

@JsonClass(generateAdapter = true)
data class TokenData(
    @Json(name = "address")
    val address: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "symbol")
    val symbol: String?,
    @Json(name = "logo")
    val logo: String?,
    @Json(name = "price")
    val price: Double?,
    @Json(name = "market_cap")
    val marketCap: Double?,
    @Json(name = "volume_24h")
    val volume24h: Double?,
    @Json(name = "liquidity")
    val liquidity: Double?,
    @Json(name = "holder_count")
    val holderCount: Int?,
    @Json(name = "buy_volume")
    val buyVolume: Double?,
    @Json(name = "sell_volume")
    val sellVolume: Double?,
    @Json(name = "created_timestamp")
    val createdTimestamp: Long?
)

@JsonClass(generateAdapter = true)
data class RankResponse(
    @Json(name = "data")
    val data: RankData?
)

@JsonClass(generateAdapter = true)
data class RankData(
    @Json(name = "rank")
    val rank: List<RankItem>?
)

@JsonClass(generateAdapter = true)
data class RankItem(
    @Json(name = "address")
    val address: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "symbol")
    val symbol: String?,
    @Json(name = "logo")
    val logo: String?,
    @Json(name = "price")
    val price: Double?,
    @Json(name = "market_cap")
    val marketCap: Double?,
    @Json(name = "volume")
    val volume: Double?,
    @Json(name = "swaps")
    val swaps: Int?,
    @Json(name = "unique_wallet")
    val uniqueWallet: Int?,
    @Json(name = "buy_count")
    val buyCount: Int?,
    @Json(name = "sell_count")
    val sellCount: Int?
)

@JsonClass(generateAdapter = true)
data class TopBuyersResponse(
    @Json(name = "data")
    val data: TopBuyersData?
)

@JsonClass(generateAdapter = true)
data class TopBuyersData(
    @Json(name = "buyers")
    val buyers: List<BuyerInfo>?
)

@JsonClass(generateAdapter = true)
data class BuyerInfo(
    @Json(name = "address")
    val address: String,
    @Json(name = "volume")
    val volume: Double?,
    @Json(name = "count")
    val count: Int?
)

@JsonClass(generateAdapter = true)
data class PriceHistoryResponse(
    @Json(name = "data")
    val data: PriceHistoryData?
)

@JsonClass(generateAdapter = true)
data class PriceHistoryData(
    @Json(name = "history")
    val history: List<PricePoint>?
)

@JsonClass(generateAdapter = true)
data class PricePoint(
    @Json(name = "timestamp")
    val timestamp: Long,
    @Json(name = "price")
    val price: Double,
    @Json(name = "volume")
    val volume: Double?
)
