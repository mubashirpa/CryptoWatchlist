package com.example.cryptowatchlist.domain.usecase

import com.example.cryptowatchlist.BuildConfig
import com.example.cryptowatchlist.data.mapper.toCoinDomainModel
import com.example.cryptowatchlist.data.mapper.toCoinEntityList
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWatchlistUseCase(
    private val repository: CoinAssetsRepository,
) {
    var priceUpdated = false

    operator fun invoke(): Flow<List<Coin>> =
        flow {
            try {
                repository.getCoins().collect {
                    emit(it.map { it.toCoinDomainModel() })
                    if (!priceUpdated) {
                        val token = BuildConfig.COIN_CAP_API_KEY
                        val ids = it.joinToString(",") { it.id }
                        val latest =
                            repository.getCoinAssets(token = token, ids = ids).toCoinEntityList()
                        repository.insertCoins(latest)
                        priceUpdated = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}
