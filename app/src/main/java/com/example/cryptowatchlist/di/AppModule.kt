package com.example.cryptowatchlist.di

import androidx.room.Room
import com.example.cryptowatchlist.core.Constants
import com.example.cryptowatchlist.data.local.database.CoinDatabase
import com.example.cryptowatchlist.data.remote.api.CoinCapService
import com.example.cryptowatchlist.data.repository.CoinAssetsRepositoryImpl
import com.example.cryptowatchlist.domain.repository.CoinAssetsRepository
import com.example.cryptowatchlist.domain.usecase.AddCoinToWatchlistUseCase
import com.example.cryptowatchlist.domain.usecase.DeleteCoinFromWatchlistUseCase
import com.example.cryptowatchlist.domain.usecase.GetCoinAssetsUseCase
import com.example.cryptowatchlist.domain.usecase.GetWatchlistUseCase
import com.example.cryptowatchlist.presentation.home.HomeViewModel
import com.example.cryptowatchlist.presentation.watchlist.WatchlistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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
        single {
            Room
                .databaseBuilder(
                    context = androidContext(),
                    klass = CoinDatabase::class.java,
                    name = "coin-database",
                ).build()
        }
        single { get<CoinDatabase>().coinAssetsDao() }
        singleOf(::CoinAssetsRepositoryImpl) { bind<CoinAssetsRepository>() }
        single { GetCoinAssetsUseCase(get()) }
        single { AddCoinToWatchlistUseCase(get()) }
        single { GetWatchlistUseCase(get()) }
        single { DeleteCoinFromWatchlistUseCase(get()) }
        viewModelOf(::HomeViewModel)
        viewModelOf(::WatchlistViewModel)
    }
