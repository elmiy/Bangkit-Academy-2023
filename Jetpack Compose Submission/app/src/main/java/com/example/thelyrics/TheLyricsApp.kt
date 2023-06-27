package com.example.thelyrics

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.thelyrics.ui.detail.DetailScreen
import com.example.thelyrics.ui.favorite.FavoriteScreen
import com.example.thelyrics.ui.home.HomeScreen
import com.example.thelyrics.ui.navigation.NavigationItem
import com.example.thelyrics.ui.navigation.Screen
import com.example.thelyrics.ui.profile.ProfileScreen
import com.example.thelyrics.ui.theme.Secondary1
import com.example.thelyrics.ui.theme.TheLyricsTheme

@Composable
fun TheLyricsApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { idSong ->
                        navController.navigate(Screen.Detail.createRoute(idSong))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    navigateToDetail = { idSong ->
                        navController.navigate(Screen.Detail.createRoute(idSong))
                    }
                )
            }
            composable(Screen.About.route) {
                ProfileScreen(
                    name = stringResource(R.string.name_elmi),
                    email = stringResource(R.string.email_elmi),
                    photo = stringResource(R.string.photo_elmi)
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("idSongs") { type = NavType.IntType }),
            ) {
                val id = it.arguments?.getInt("idSongs")
                DetailScreen(
                    idSong = id as Int,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Screen.Home,
            ),
            NavigationItem(
                title = stringResource(R.string.menu_favorite),
                icon = Icons.Default.Favorite,
                screen = Screen.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_about),
                icon = Icons.Default.AccountCircle,
                screen = Screen.About
            ),
        )
        BottomNavigation (
            backgroundColor = Color.White,
            contentColor = Secondary1
        ){
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        when (item.title) {
                            stringResource(R.string.title_home) -> {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(R.string.home_page)
                                )
                            }
                            stringResource(R.string.title_favorite) -> {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(R.string.favorite_page)
                                )
                            }
                            stringResource(R.string.title_about) -> {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(R.string.about_page)
                                )
                            }
                        }
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TheLyricsAppPreview() {
    TheLyricsTheme {
        TheLyricsApp()
    }
}