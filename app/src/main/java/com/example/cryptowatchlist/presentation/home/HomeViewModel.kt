package com.example.cryptowatchlist.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.cryptowatchlist.domain.model.Coin
import com.example.cryptowatchlist.domain.usecase.GetCoinAssetsPagingUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCoinAssetsPagingUseCase: GetCoinAssetsPagingUseCase,
) : ViewModel() {
    private val _coins = MutableLiveData<PagingData<Coin>>()
    val coins: LiveData<PagingData<Coin>> = _coins

    init {
        getCoins()
    }

    private fun getCoins() {
        viewModelScope.launch {
            getCoinAssetsPagingUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    _coins.value = it
                }
        }
    }
}
