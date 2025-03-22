package com.example.cryptowatchlist.presentation.home

import com.example.cryptowatchlist.domain.model.Coin

sealed class HomeUiEvent {
    data class AddCoinToWatchlist(
        val coin: Coin,
    ) : HomeUiEvent()

    data class Search(
        val query: String,
        val delay: Long = 0,
    ) : HomeUiEvent()
}
