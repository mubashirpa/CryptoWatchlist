package com.example.cryptowatchlist.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinAssetsDto(
    val `data`: List<CoinDto>? = null,
    val timestamp: Long? = null,
)
