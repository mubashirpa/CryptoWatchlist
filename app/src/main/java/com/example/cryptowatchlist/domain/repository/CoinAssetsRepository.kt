package com.example.cryptowatchlist.domain.repository

import androidx.paging.PagingData
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import kotlinx.coroutines.flow.Flow

interface CoinAssetsRepository {
    suspend fun getCoinAssets(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): CoinAssetsDto

    suspend fun getCoinAssetsPaging(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): Flow<PagingData<CoinEntity>>

    suspend fun insertCoin(coin: CoinEntity)

    fun getWatchlist(): Flow<List<CoinEntity>>
}
