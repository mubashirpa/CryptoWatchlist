package com.example.cryptowatchlist.presentation.home

sealed class HomeUiEvent {
    data class Search(
        val query: String,
        val delay: Long = 0,
    ) : HomeUiEvent()
}
