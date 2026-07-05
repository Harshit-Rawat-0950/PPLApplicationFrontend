package com.ferhatozcelik.jetpackcomposetemplate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ferhatozcelik.jetpackcomposetemplate.ui.detail.DetailScreen
import com.ferhatozcelik.jetpackcomposetemplate.ui.home.MainScreen
import com.ferhatozcelik.jetpackcomposetemplate.ui.screens.DashboardScreen
import com.ferhatozcelik.jetpackcomposetemplate.ui.screens.LoginScreen
import com.ferhatozcelik.jetpackcomposetemplate.ui.screens.NearMissScreen
import com.ferhatozcelik.jetpackcomposetemplate.ui.screens.ShiftLogbookScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController, startDestination = Screen.Login.route
    ) {

        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(
            "${Screen.Detail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            DetailScreen(navController = navController, id = it.arguments?.getInt("id") ?: 0)
        }
        composable(Screen.ShiftLogbook.route) {
            ShiftLogbookScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.NearMiss.route) {
            NearMissScreen(navController = navController)
        }
        composable(Screen.PastLogbooks.route) {
            com.ferhatozcelik.jetpackcomposetemplate.ui.screens.PastLogbooksScreen(navController = navController)
        }
        composable(Screen.LogbookDashboard.route) {
            com.ferhatozcelik.jetpackcomposetemplate.ui.screens.LogbookDashboardScreen(navController = navController)
        }
        composable(Screen.NearMissDashboard.route) {
            com.ferhatozcelik.jetpackcomposetemplate.ui.screens.NearMissDashboardScreen(navController = navController)
        }
        composable(Screen.PastNearMisses.route) {
            com.ferhatozcelik.jetpackcomposetemplate.ui.screens.PastNearMissesScreen(navController = navController)
        }
        composable(
            route = Screen.LogbookDetail.route,
            arguments = listOf(androidx.navigation.navArgument("logbookId") { type = androidx.navigation.NavType.IntType })
        ) { backStackEntry ->
            val logbookId = backStackEntry.arguments?.getInt("logbookId") ?: 0
            com.ferhatozcelik.jetpackcomposetemplate.ui.screens.LogbookDetailScreen(navController = navController, logbookId = logbookId)
        }
    }
}
