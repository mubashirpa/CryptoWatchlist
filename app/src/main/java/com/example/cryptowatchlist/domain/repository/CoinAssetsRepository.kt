package com.example.cryptowatchlist.domain.repository

import androidx.paging.PagingData
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import com.example.cryptowatchlist.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinAssetsRepository {
    suspend fun getCoinAssets(
        token: String,
        search: String? = null,
        ids: List<String>? = null,
    ): CoinAssetsDto

    suspend fun getCoinAssets(
        token: String,
        search: String? = null,
        ids: List<String>? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): Flow<PagingData<Coin>>

    suspend fun insertCoin(coin: CoinEntity)

    fun getWatchlist(): Flow<List<CoinEntity>>
}
