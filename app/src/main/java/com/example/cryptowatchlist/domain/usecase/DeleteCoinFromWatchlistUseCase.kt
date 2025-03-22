package com.example.cryptowatchlist.domain.usecase

import com.example.cryptowatchlist.R
import com.example.cryptowatchlist.core.Result
import com.example.cryptowatchlist.core.UiText
import com.example.cryptowatchlist.data.mapper.toCoinEntity
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DeleteCoinFromWatchlistUseCase(
    private val repository: CoinAssetsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(coin: Coin): Flow<Result<Boolean>> =
        flow {
            try {
                emit(Result.Loading())
                repository.deleteCoin(coin.toCoinEntity())
                emit(Result.Success(true))
            } catch (e: Exception) {
                val message =
                    e.message?.let {
                        UiText.DynamicString(it)
                    } ?: UiText.StringResource(R.string.error_unknown)
                emit(Result.Error(message))
            }
        }.flowOn(ioDispatcher)
}
