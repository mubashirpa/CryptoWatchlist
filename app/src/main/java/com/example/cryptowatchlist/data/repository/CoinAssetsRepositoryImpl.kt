package com.example.cryptowatchlist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
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
    override suspend fun getCoinAssets(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): CoinAssetsDto = api.getAssets(search, ids, limit, offset)

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getCoinAssetsPaging(
        search: String?,
        ids: List<String>?,
        limit: Int?,
        offset: Int?,
    ): Flow<PagingData<CoinEntity>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = limit ?: NETWORK_PAGE_SIZE,
                    enablePlaceholders = false,
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
                database.coinAssetsDao().pagingSource()
            },
        ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}
