package com.example.cryptowatchlist

import android.app.Application
import com.example.cryptowatchlist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CryptoWatchlistApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CryptoWatchlistApplication)
            modules(appModule)
        }
    }
}
