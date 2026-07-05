package com.ferhatozcelik.jetpackcomposetemplate.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Detail : Screen("detail_screen")
    object ShiftLogbook : Screen("shift_logbook_screen")
    object Login : Screen("login_screen")
    object Dashboard : Screen("dashboard_screen")
    object NearMiss : Screen("near_miss_screen")
    object PastLogbooks : Screen("past_logbooks_screen")
    object LogbookDashboard : Screen("logbook_dashboard_screen")
    object NearMissDashboard : Screen("near_miss_dashboard_screen")
    object PastNearMisses : Screen("past_near_misses_screen")
    object LogbookDetail : Screen("logbook_detail/{logbookId}") {
        fun createRoute(logbookId: Int) = "logbook_detail/$logbookId"
    }
}