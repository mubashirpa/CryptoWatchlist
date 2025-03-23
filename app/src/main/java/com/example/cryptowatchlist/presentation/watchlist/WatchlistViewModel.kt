package com.example.cryptowatchlist.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.usecase.DeleteCoinFromWatchlistUseCase
import com.example.cryptowatchlist.domain.usecase.GetWatchlistUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val deleteCoinFromWatchlistUseCase: DeleteCoinFromWatchlistUseCase,
) : ViewModel() {
    private val _watchlist: MutableLiveData<List<Coin>> = MutableLiveData()
    val watchlist: LiveData<List<Coin>> = _watchlist

    init {
        getWatchlist()
    }

    fun onEvent(event: WatchlistUiEvent) {
        when (event) {
            is WatchlistUiEvent.DeleteCoinFromWatchlist -> {
                deleteCoinFromWatchlist(event.coin)
            }
        }
    }

    private fun getWatchlist() {
        viewModelScope.launch {
            getWatchlistUseCase().collect {
                _watchlist.value = it
            }
        }
    }

    private fun deleteCoinFromWatchlist(coin: Coin) {
        deleteCoinFromWatchlistUseCase(coin).launchIn(viewModelScope)
    }
}
