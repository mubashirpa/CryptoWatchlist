package com.example.cryptowatchlist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cryptowatchlist.data.local.dao.CoinAssetsDao
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import com.example.cryptowatchlist.data.remote.paging.CoinAssetsPagingSource
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.flow.Flow

class CoinAssetsRepositoryImpl(
    private val api: CoinCapService,
    private val coinAssetsDao: CoinAssetsDao,
) : CoinAssetsRepository {
    override suspend fun getCoinAssets(
        token: String,
        search: String?,
        ids: List<String>?,
    ): CoinAssetsDto = api.getAssets(token, search, ids, null, null)

    override suspend fun getCoinAssets(
        token: String,
        search: String?,
        ids: List<String>?,
        limit: Int,
        offset: Int,
    ): Flow<PagingData<Coin>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = limit,
                    enablePlaceholders = false,
                    initialLoadSize = 2 * limit,
                ),
            pagingSourceFactory = {
                CoinAssetsPagingSource(api, token, search, ids, offset)
            },
        ).flow

    override suspend fun insertCoin(coin: CoinEntity) = coinAssetsDao.upsertCoin(coin)

    override fun getCoins(): Flow<List<CoinEntity>> = coinAssetsDao.getCoins()

    override suspend fun deleteCoin(coin: CoinEntity) = coinAssetsDao.deleteCoin(coin)
}
