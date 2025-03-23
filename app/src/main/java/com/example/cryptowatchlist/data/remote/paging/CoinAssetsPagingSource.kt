package com.example.cryptowatchlist.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cryptowatchlist.data.mapper.toCoinDomainModelList
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.domain.model.Coin
import retrofit2.HttpException
import java.io.IOException

class CoinAssetsPagingSource(
    private val api: CoinCapService,
    private val token: String,
    private val search: String? = null,
    private val ids: String? = null,
    private val offset: Int = 0,
) : PagingSource<Int, Coin>() {
    override fun getRefreshKey(state: PagingState<Int, Coin>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coin> =
        try {
            val limit = params.loadSize
            val nextOffset = params.key ?: offset

            val response =
                api.getAssets(
                    token = token,
                    search = search,
                    ids = ids,
                    limit = limit,
                    offset = nextOffset,
                )
            val data = response.toCoinDomainModelList()

            val nextKey =
                if (data.isNotEmpty() && data.size == limit) {
                    nextOffset + limit
                } else {
                    null
                }
            val prevKey =
                if (nextOffset > 0) {
                    maxOf(0, nextOffset - limit)
                } else {
                    null
                }

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}
