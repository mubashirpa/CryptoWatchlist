package com.example.cryptowatchlist.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.cryptowatchlist.data.mapper.toCoinDomainModel
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetCoinAssetsPagingUseCase(
    private val repository: CoinAssetsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend operator fun invoke(
        search: String? = null,
        ids: List<String>? = null,
        limit: Int? = 20,
        offset: Int? = 0,
    ): Flow<PagingData<Coin>> =
        withContext(ioDispatcher) {
            repository.getCoinAssetsPaging(search, ids, limit, offset).map { pagingData ->
                pagingData.map {
                    it.toCoinDomainModel()
                }
            }
        }
}
