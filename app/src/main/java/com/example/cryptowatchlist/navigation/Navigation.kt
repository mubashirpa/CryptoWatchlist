package com.example.cryptowatchlist.navigation

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.example.cryptowatchlist.R
import com.example.cryptowatchlist.presentation.home.HomeFragment
import com.example.cryptowatchlist.presentation.watchlist.WatchlistFragment

fun AppCompatActivity.findNavController(
    @IdRes viewId: Int,
    startDestination: Screen = Screen.Home,
): NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(viewId) as NavHostFragment
    val navController = navHostFragment.navController

    navController.graph =
        navController.createGraph(startDestination = startDestination) {
            fragment<HomeFragment, Screen.Home> {
                label = getString(R.string.label_home_screen)
            }
            fragment<WatchlistFragment, Screen.Watchlist> {
                label = getString(R.string.label_watchlist_screen)
            }
        }

    return navController
}
