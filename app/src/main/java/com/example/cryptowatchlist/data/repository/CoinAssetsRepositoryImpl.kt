package com.example.cryptowatchlist.data.repository

import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository

class CoinAssetsRepositoryImpl(
    private val api: CoinCapService,
) : CoinAssetsRepository {
    override suspend fun getCoinAssets(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): CoinAssetsDto = api.getAssets(search, ids, limit, offset)
}
