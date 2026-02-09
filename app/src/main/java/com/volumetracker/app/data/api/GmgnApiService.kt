package com.volumetracker.app.data.api

import com.volumetracker.app.data.api.dto.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GmgnApiService {
    
    @GET("defi/quotation/v1/tokens/sol/{address}")
    suspend fun getToken(
        @Path("address") address: String
    ): Response<TokenResponse>
    
    @GET("defi/quotation/v1/rank/sol/swaps/{timeframe}")
    suspend fun getRankBySwaps(
        @Path("timeframe") timeframe: String, // 5m, 1h, 6h, 24h
        @Query("orderby") orderBy: String = "swaps",
        @Query("direction") direction: String = "desc",
        @Query("limit") limit: Int = 50
    ): Response<RankResponse>
    
    @GET("defi/quotation/v1/tokens/top_buyers/sol/{address}")
    suspend fun getTopBuyers(
        @Path("address") address: String,
        @Query("period") period: String = "24h",
        @Query("limit") limit: Int = 20
    ): Response<TopBuyersResponse>
    
    @GET("defi/quotation/v1/tokens/price_history/sol/{address}")
    suspend fun getPriceHistory(
        @Path("address") address: String,
        @Query("period") period: String = "1h", // 5m, 15m, 1h, 4h, 1d
        @Query("limit") limit: Int = 100
    ): Response<PriceHistoryResponse>
}
