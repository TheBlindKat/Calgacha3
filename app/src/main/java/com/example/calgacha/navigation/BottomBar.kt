package com.example.calgacha.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.*


sealed class BottomNavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem(Routes.HOME, "Inicio", Icons.Default.Home)
    object Add : BottomNavItem(Routes.ADD, "Añadir gallina", Icons.Default.Add)
    object History : BottomNavItem(Routes.HISTORY, "Historial", Icons.Default.Info)
    object Profile : BottomNavItem(Routes.PROFILE, "Perfil", Icons.Default.Person)

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val icons = listOf(Icons.Default.Home, Icons.Default.Add, Icons.Default.Info, Icons.Default.Person)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = null) },
                    label = { Text(screen.route.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
