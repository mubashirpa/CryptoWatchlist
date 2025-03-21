package com.example.cryptowatchlist.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.cryptowatchlist.BuildConfig
import com.example.cryptowatchlist.data.local.database.CoinDatabase
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.local.entity.UpdateTimeEntity
import com.example.cryptowatchlist.data.mapper.toCoinEntityList
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CoinAssetsRemoteMediator(
    private val api: CoinCapService,
    private val database: CoinDatabase,
    private val search: String?,
    private val ids: List<String>?,
    private val offset: Int?,
) : RemoteMediator<Int, CoinEntity>() {
    private val coinAssetsDao = database.coinAssetsDao()
    private val updateTimeDao = database.updateTimeDao()

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = updateTimeDao.getUpdateTimeById(0)?.lastUpdated ?: 0
        return if (isCacheValid(lastUpdated)) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>,
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> {
                        offset
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        val last = state.lastItemOrNull()
                        if (last == null) {
                            offset
                        } else {
                            last.rank?.toIntOrNull()?.plus(1)
                        }
                    }
                }

            val response =
                api.getAssets(
                    token = "Bearer ${BuildConfig.COIN_CAP_API_KEY}",
                    search = search,
                    ids = ids,
                    limit = state.config.pageSize,
                    offset = loadKey,
                )
            val coins = response.toCoinEntityList()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    updateTimeDao.deleteUpdateTimeById(0)
                    coinAssetsDao.clearAll()
                }

                coinAssetsDao.insertAll(coins)
                updateTimeDao.upsertUpdateTime(
                    UpdateTimeEntity(
                        id = 0,
                        lastUpdated = System.currentTimeMillis(),
                    ),
                )
            }

            val endOfPaginationReached = coins.isEmpty() || coins.size < state.config.pageSize
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    fun isCacheValid(lastUpdated: Long): Boolean {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return System.currentTimeMillis() - lastUpdated <= cacheTimeout
    }
}
