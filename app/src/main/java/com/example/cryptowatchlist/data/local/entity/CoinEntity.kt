package com.example.cryptowatchlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val changePercent24Hr: String? = null,
    val explorer: String? = null,
    val marketCapUsd: String? = null,
    val maxSupply: String? = null,
    val name: String? = null,
    val priceUsd: String? = null,
    val rank: String? = null,
    val supply: String? = null,
    val symbol: String? = null,
    val volumeUsd24Hr: String? = null,
    val vwap24Hr: String? = null,
)
