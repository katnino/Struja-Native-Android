package com.noniboy.struja.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.noniboy.struja.ui.screens.bill.BillScreen
import com.noniboy.struja.ui.screens.home.HomeScreen
import com.noniboy.struja.ui.screens.meter.MeterScreen
import com.noniboy.struja.ui.screens.reading.ReadingScreen

@Composable
fun StrujaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable(
            route = "meters/{meterId}",
            arguments = listOf(navArgument("meterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meterId = backStackEntry.arguments?.getString("meterId") ?: return@composable
            MeterScreen(
                navController = navController,
                meterId = meterId
            )
        }

        composable(
            route = "meters/{meterId}/readings/new",
            arguments = listOf(navArgument("meterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val meterId = backStackEntry.arguments?.getString("meterId") ?: return@composable
            ReadingScreen(
                navController = navController,
                meterId = meterId
            )
        }

        composable(
            route = "meters/{meterId}/bills/{billId}",
            arguments = listOf(
                navArgument("meterId") { type = NavType.StringType },
                navArgument("billId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val meterId = backStackEntry.arguments?.getString("meterId") ?: return@composable
            val billId = backStackEntry.arguments?.getString("billId") ?: return@composable
            BillScreen(
                navController = navController,
                meterId = meterId,
                billId = billId
            )
        }
    }
}
