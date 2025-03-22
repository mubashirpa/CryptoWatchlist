package com.example.cryptowatchlist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinAssetsDao {
    @Upsert
    suspend fun upsertCoin(coin: CoinEntity)

    @Query("SELECT * FROM coins")
    fun getCoins(): Flow<List<CoinEntity>>

    @Delete
    suspend fun deleteCoin(coin: CoinEntity)
}
