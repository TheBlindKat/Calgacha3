package com.example.calgacha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calgacha.data.local.AppDatabase
import com.example.calgacha.data.remote.RetrofitInstance
import com.example.calgacha.data.repository.UserPreferencesRepository
import com.example.calgacha.data.repository.ChickenRepository
import com.example.calgacha.di.ViewModelFactory
import com.example.calgacha.navigation.BottomBar
import com.example.calgacha.navigation.BottomNavItem
import com.example.calgacha.navigation.Routes
import com.example.calgacha.ui.screens.AddScreen
import com.example.calgacha.ui.screens.DetailScreen
import com.example.calgacha.ui.screens.HistoryDetailScreen
import com.example.calgacha.ui.screens.HistoryScreen
import com.example.calgacha.ui.screens.HomeScreen
import com.example.calgacha.ui.screens.ProfileScreen
import com.example.calgacha.ui.theme.NavigationTheme
import com.example.calgacha.ui.viewmodel.AddViewModel
import com.example.calgacha.ui.viewmodel.LoginViewModel
import com.example.calgacha.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        // Retrofit API
        val api = RetrofitInstance.api

        //  Repositorio DAO/ API
        val chickenRepository = ChickenRepository(
            chickenDao = database.chickenDao(),
            api = api
        )

        val userPreferencesRepository = UserPreferencesRepository(applicationContext)

        val factory = ViewModelFactory(
            chickenRepository = chickenRepository,
            userPreferencesRepository = userPreferencesRepository
        )

        enableEdgeToEdge()
        setContent {
            NavigationTheme {
                App(factory = factory)
            }
        }
    }
}


@Composable
fun App(factory: ViewModelFactory) {
    val navController = rememberNavController()

    val mainViewModel: MainViewModel = viewModel(factory = factory)
    val addViewModel: AddViewModel = viewModel(factory = factory)
    val loginViewModel: LoginViewModel = viewModel(factory = factory)

    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Add,
        BottomNavItem.History,
        BottomNavItem.Profile,
    )

    Scaffold(
        bottomBar = { BottomBar(navController, bottomItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = mainViewModel,
                    navController = navController
                )
            }

            composable(Routes.ADD) {
                AddScreen(vm = addViewModel, navController = navController)
            }

            composable(Routes.HISTORY) {
                HistoryScreen(
                    viewModel = mainViewModel,
                    navController = navController,
                    onItemClick = { itemId ->
                        navController.navigate(Routes.historyDetailRoute(itemId))
                    }
                )
            }


            composable(
                route = Routes.HISTORY_DETAIL,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("itemId") ?: -1
                HistoryDetailScreen(
                    itemId = id,
                    viewModel = mainViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(viewModel = loginViewModel)
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("chickenId") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("chickenId") ?: -1
                DetailScreen(
                    id = id,
                    viewModel = mainViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
