package com.example.cryptowatchlist.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.usecase.GetWatchlistUseCase
import kotlinx.coroutines.launch

class WatchlistViewModel(
    private val getWatchlistUseCase: GetWatchlistUseCase,
) : ViewModel() {
    private val _watchlist: MutableLiveData<List<Coin>> = MutableLiveData(emptyList())
    val watchlist: LiveData<List<Coin>> = _watchlist

    init {
        getWatchlist()
    }

    private fun getWatchlist() {
        viewModelScope.launch {
            getWatchlistUseCase().collect {
                _watchlist.value = it
            }
        }
    }
}
