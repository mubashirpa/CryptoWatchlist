package com.example.cryptowatchlist.domain.repository

import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto

interface CoinAssetsRepository {
    suspend fun getCoinAssets(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): CoinAssetsDto
}
