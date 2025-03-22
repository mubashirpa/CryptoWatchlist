package com.example.cryptowatchlist.domain.usecase

import com.example.cryptowatchlist.data.mapper.toCoinDomainModel
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWatchlistUseCase(
    private val repository: CoinAssetsRepository,
) {
    operator fun invoke(): Flow<List<Coin>> =
        repository.getCoins().map {
            it.map { it.toCoinDomainModel() }
        }
}
