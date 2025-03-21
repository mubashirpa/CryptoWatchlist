package com.example.cryptowatchlist.di

import com.example.cryptowatchlist.core.Constants
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.data.repository.CoinAssetsRepositoryImpl
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import com.example.cryptowatchlist.domain.usecase.GetCoinAssetsUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule =
    module {
        single {
            Retrofit
                .Builder()
                .baseUrl(Constants.COIN_CAP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinCapService::class.java)
        }
        singleOf(::CoinAssetsRepositoryImpl) { bind<CoinAssetsRepository>() }
        single { GetCoinAssetsUseCase(get()) }
    }
