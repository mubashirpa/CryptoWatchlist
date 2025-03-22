package com.example.cryptowatchlist.domain.model

data class Coin(
    val changePercent24Hr: String? = null,
    val explorer: String? = null,
    val id: String? = null,
    val isInWatchlist: Boolean? = null,
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
