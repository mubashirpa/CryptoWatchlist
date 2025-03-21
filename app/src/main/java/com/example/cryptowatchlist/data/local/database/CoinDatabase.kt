package com.example.cryptowatchlist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptowatchlist.data.local.dao.CoinAssetsDao
import com.example.cryptowatchlist.data.local.dao.UpdateTimeDao
import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.local.entity.UpdateTimeEntity

@Database(
    entities = [CoinEntity::class, UpdateTimeEntity::class],
    version = 1,
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinAssetsDao(): CoinAssetsDao

    abstract fun updateTimeDao(): UpdateTimeDao
}
