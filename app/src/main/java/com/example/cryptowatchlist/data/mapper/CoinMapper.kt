package com.example.cryptowatchlist.data.mapper

import com.example.cryptowatchlist.data.local.entity.CoinEntity
import com.example.cryptowatchlist.data.remote.dto.CoinAssetsDto
import com.example.cryptowatchlist.data.remote.dto.CoinDto
import com.example.cryptowatchlist.domain.model.Coin

fun CoinAssetsDto.toCoinDomainModelList(): List<Coin> = `data`?.map { it.toCoinDomainModel() } ?: emptyList()

fun CoinDto.toCoinDomainModel(): Coin =
    Coin(
        changePercent24Hr = changePercent24Hr,
        explorer = explorer,
        id = id,
        marketCapUsd = marketCapUsd,
        maxSupply = maxSupply,
        name = name,
        priceUsd = priceUsd,
        rank = rank,
        supply = supply,
        symbol = symbol,
        volumeUsd24Hr = volumeUsd24Hr,
        vwap24Hr = vwap24Hr,
    )

fun CoinAssetsDto.toCoinEntityList(): List<CoinEntity> = `data`?.map { it.toCoinEntity() } ?: emptyList()

fun CoinDto.toCoinEntity(): CoinEntity =
    CoinEntity(
        id = id!!,
        changePercent24Hr = changePercent24Hr,
        explorer = explorer,
        marketCapUsd = marketCapUsd,
        maxSupply = maxSupply,
        name = name,
        priceUsd = priceUsd,
        rank = rank,
        supply = supply,
        symbol = symbol,
        volumeUsd24Hr = volumeUsd24Hr,
        vwap24Hr = vwap24Hr,
    )

fun CoinEntity.toCoinDomainModel(): Coin =
    Coin(
        changePercent24Hr = changePercent24Hr,
        explorer = explorer,
        id = id,
        marketCapUsd = marketCapUsd,
        maxSupply = maxSupply,
        name = name,
        priceUsd = priceUsd,
        rank = rank,
        supply = supply,
        symbol = symbol,
        volumeUsd24Hr = volumeUsd24Hr,
        vwap24Hr = vwap24Hr,
    )

fun Coin.toCoinEntity(): CoinEntity =
    CoinEntity(
        id = id!!,
        changePercent24Hr = changePercent24Hr,
        explorer = explorer,
        marketCapUsd = marketCapUsd,
        maxSupply = maxSupply,
        name = name,
        priceUsd = priceUsd,
        rank = rank,
        supply = supply,
        symbol = symbol,
        volumeUsd24Hr = volumeUsd24Hr,
        vwap24Hr = vwap24Hr,
    )
