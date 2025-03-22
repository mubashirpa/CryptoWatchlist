package com.example.cryptowatchlist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptowatchlist.data.local.dao.CoinAssetsDao
import com.example.cryptowatchlist.data.local.entity.CoinEntity

@Database(
    entities = [CoinEntity::class],
    version = 1,
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinAssetsDao(): CoinAssetsDao
}
