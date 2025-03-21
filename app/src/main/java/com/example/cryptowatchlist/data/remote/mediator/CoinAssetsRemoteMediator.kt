package com.example.cryptowatchlist.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.cryptowatchlist.data.local.database.CoinDatabase
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.mapper.toCoinEntityList
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CoinAssetsRemoteMediator(
    private val api: CoinCapService,
    private val database: CoinDatabase,
    private val search: String?,
    private val ids: List<String>?,
    private val offset: Int?,
) : RemoteMediator<Int, CoinEntity>() {
    private val dao = database.coinAssetsDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>,
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> offset

                    LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                    LoadType.APPEND -> {
                        state.lastItemOrNull()
                            ?: return MediatorResult.Success(endOfPaginationReached = true)
                        state.pages.sumOf { it.data.size }
                    }
                }

            val response =
                api.getAssets(
                    search = search,
                    ids = ids,
                    limit = state.config.pageSize,
                    offset = loadKey,
                )
            val coins = response.toCoinEntityList()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }

                dao.insertAll(coins)
            }

            val endOfPaginationReached = coins.isEmpty() || coins.size < state.config.pageSize
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
