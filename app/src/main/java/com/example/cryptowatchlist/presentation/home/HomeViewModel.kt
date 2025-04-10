package com.example.cryptowatchlist.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cryptowatchlist.core.Result
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.usecase.AddCoinToWatchlistUseCase
import com.example.cryptowatchlist.domain.usecase.GetCoinAssetsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCoinAssetsUseCase: GetCoinAssetsUseCase,
    private val addCoinToWatchlistUseCase: AddCoinToWatchlistUseCase,
) : ViewModel() {
    private val _coins = MutableLiveData<PagingData<Coin>>()
    val coins: LiveData<PagingData<Coin>> = _coins

    private val _coinsResult: MutableLiveData<Result<List<Coin>>> = MutableLiveData(Result.Empty())
    val coinsResult: LiveData<Result<List<Coin>>> = _coinsResult

    private var searchCoinUseCaseJob: Job? = null

    init {
        getCoins()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.AddCoinToWatchlist -> {
                addCoinToWatchList(event.coin)
            }

            is HomeUiEvent.Search -> {
                searchCoin(event.query.trim(), event.delay)
            }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            getCoinAssetsUseCase(limit = 20, offset = 0)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _coins.value = it
                }
        }
    }

    private fun searchCoin(
        query: String,
        delay: Long = 0,
    ) {
        searchCoinUseCaseJob?.cancel()
        searchCoinUseCaseJob = null

        if (query.isBlank()) {
            _coinsResult.value = Result.Empty()
            return
        }

        searchCoinUseCaseJob =
            viewModelScope.launch {
                delay(delay)
                getCoinAssetsUseCase(query)
                    .onEach { result ->
                        _coinsResult.value = result
                    }.launchIn(this)
            }
    }

    private fun addCoinToWatchList(coin: Coin) {
        addCoinToWatchlistUseCase(coin).launchIn(viewModelScope)
    }
}
