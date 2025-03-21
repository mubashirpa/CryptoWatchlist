package com.example.cryptowatchlist.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.cryptowatchlist.data.local.entity.CoinEntity

@Dao
interface CoinAssetsDao {
    @Upsert
    suspend fun insertAll(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins")
    fun pagingSource(): PagingSource<Int, CoinEntity>

    @Query("DELETE FROM coins")
    suspend fun clearAll()
}
