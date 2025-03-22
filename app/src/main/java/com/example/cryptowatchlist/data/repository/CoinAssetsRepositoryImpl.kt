package com.example.cryptowatchlist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cryptowatchlist.BuildConfig
import com.example.cryptowatchlist.data.local.database.CoinDatabase
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import com.example.cryptowatchlist.data.remote.mediator.CoinAssetsRemoteMediator
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.flow.Flow

class CoinAssetsRepositoryImpl(
    private val api: CoinCapService,
    private val database: CoinDatabase,
) : CoinAssetsRepository {
    private val coinAssetsDao = database.coinAssetsDao()

    override suspend fun getCoinAssets(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): CoinAssetsDto = api.getAssets(BuildConfig.COIN_CAP_API_KEY, search, ids, limit, offset)

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getCoinAssetsPaging(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): Flow<PagingData<CoinEntity>> {
        val pageSize = limit ?: NETWORK_PAGE_SIZE
        return Pager(
            config =
                PagingConfig(
                    pageSize = pageSize,
                    enablePlaceholders = false,
                    initialLoadSize = 2 * pageSize,
                ),
            remoteMediator =
                CoinAssetsRemoteMediator(
                    api = api,
                    database = database,
                    search = search,
                    ids = ids,
                    offset = offset,
                ),
            pagingSourceFactory = {
                coinAssetsDao.pagingSource()
            },
        ).flow
    }

    override suspend fun insertCoin(coin: CoinEntity) = coinAssetsDao.insert(coin)

    override fun getWatchlist(): Flow<List<CoinEntity>> = coinAssetsDao.getWatchlist()

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}
