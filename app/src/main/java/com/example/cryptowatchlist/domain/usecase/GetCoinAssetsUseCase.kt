package com.example.cryptowatchlist.domain.usecase

import androidx.paging.PagingData
import com.example.cryptowatchlist.BuildConfig
import com.example.cryptowatchlist.R
import com.example.cryptowatchlist.core.Result
import com.example.cryptowatchlist.core.UiText
import com.example.cryptowatchlist.data.mapper.toCoinDomainModelList
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GetCoinAssetsUseCase(
    private val repository: CoinAssetsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val token = BuildConfig.COIN_CAP_API_KEY

    operator fun invoke(
        search: String? = null,
        ids: String? = null,
    ): Flow<Result<List<Coin>>> =
        flow {
            try {
                emit(Result.Loading())
                val coinAssets =
                    repository.getCoinAssets(token, search, ids).toCoinDomainModelList()
                emit(Result.Success(coinAssets))
            } catch (e: Exception) {
                val message =
                    e.message?.let {
                        UiText.DynamicString(it)
                    } ?: UiText.StringResource(R.string.error_unknown)
                emit(Result.Error(message))
            }
        }.flowOn(ioDispatcher)

    suspend operator fun invoke(
        search: String? = null,
        ids: String? = null,
        limit: Int = 20,
        offset: Int = 0,
    ): Flow<PagingData<Coin>> =
        withContext(ioDispatcher) {
            repository.getCoinAssets(token, search, ids, limit, offset)
        }
}
