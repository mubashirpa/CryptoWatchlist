package com.example.cryptowatchlist.presentation.watchlist

import com.example.cryptowatchlist.domain.model.Coin

sealed class WatchlistUiEvent {
    data class DeleteCoinFromWatchlist(
        val coin: Coin,
    ) : WatchlistUiEvent()
}
