package com.example.praticazero.ui.theme.nav

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.praticazero.ui.theme.HomePage
import com.example.praticazero.ui.theme.ListPage
import com.example.praticazero.ui.theme.MapPage
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavHost(navController: NavHostController,
                modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(modifier = modifier) }
        composable<Route.List> { ListPage(modifier = modifier) }
        composable<Route.Map> { MapPage(modifier = modifier) }
    }
}

