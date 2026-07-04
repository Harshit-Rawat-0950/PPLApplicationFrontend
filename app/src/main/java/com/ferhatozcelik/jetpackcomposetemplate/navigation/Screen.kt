package com.ferhatozcelik.jetpackcomposetemplate.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Detail : Screen("detail_screen")
    object ShiftLogbook : Screen("shift_logbook_screen")
    object Login : Screen("login_screen")
    object Dashboard : Screen("dashboard_screen")
    object NearMiss : Screen("near_miss_screen")
}