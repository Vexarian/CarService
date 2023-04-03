package mobile.solareye.carservice.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import mobile.solareye.carservice.ui.main.ListOrderScreen
import mobile.solareye.carservice.ui.addedit_order.AddEditOrderScreen
import mobile.solareye.carservice.ui.show_order.ShowOrderScreen
import mobile.solareye.carservice.utils.orderUri

@ExperimentalMaterial3Api
@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = "order_list",
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("order_list") {
            /*FIXME*/
            val needUpdate = navController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow("needUpdate", false)
                ?.collectAsState()

            ListOrderScreen(
                needUpdate = needUpdate?.value ?: false,
                onNavigateToOrderCreate = {
                    navController.navigate(route = "order_create")
                },
                onNavigateToOrderEdit = { orderId ->
                    navController.navigate(route = "order_edit/$orderId")
                },
                onNavigateToOrderShow = { orderId ->
                    navController.navigate(route = "order_show/$orderId")
                },
            )
        }
        composable(
            route = "order_show/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType }),
            deepLinks = listOf(navDeepLink { uriPattern = "$orderUri{orderId}" })
        ) {
            ShowOrderScreen {
                /*FIXME*/
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("needUpdate", true)
                navController.popBackStack()
            }
        }
        composable(
            route = "order_create",
        ) {
            AddEditOrderScreen {
                /*FIXME*/
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("needUpdate", true)
                navController.popBackStack()
            }
        }
        composable(
            route = "order_edit/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.IntType }),
        ) {
            AddEditOrderScreen {
                /*FIXME*/
                navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("needUpdate", true)
                navController.popBackStack()
            }
        }
    }
}