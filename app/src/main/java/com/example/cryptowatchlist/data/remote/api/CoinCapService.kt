package com.example.cryptowatchlist.data.remote.api

import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinCapService {
    @GET("v2/assets")
    suspend fun getAssets(
        @Query("search") search: String?,
        @Query("ids") ids: List<String>?,
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
    ): CoinAssetsDto
}
