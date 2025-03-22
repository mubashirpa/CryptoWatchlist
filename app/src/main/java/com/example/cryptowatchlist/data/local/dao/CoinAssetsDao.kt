package com.example.cryptowatchlist.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinAssetsDao {
    @Upsert
    suspend fun insert(coin: CoinEntity)

    @Upsert
    suspend fun insertAll(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins")
    fun pagingSource(): PagingSource<Int, CoinEntity>

    @Query("DELETE FROM coins")
    suspend fun clearAll()

    @Query("SELECT * FROM coins WHERE isInWatchlist = 1")
    fun getWatchlist(): Flow<List<CoinEntity>>
}
