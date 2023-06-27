package com.example.thelyrics.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object Detail : Screen("home/{idSongs}") {
        fun createRoute(idSongs: Int) = "home/$idSongs"
    }
}