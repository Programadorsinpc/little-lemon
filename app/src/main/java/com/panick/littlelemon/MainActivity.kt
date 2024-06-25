package com.panick.littlelemon

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.panick.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
                MyNavigation()
            }
        }
    }
}

@Composable
fun MyNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLogIn = sharedPreferences.getBoolean("is_logged_in", false)
    val startDestination = if (isLogIn) HomeDestination.route else OnboardingDestination.route
    NavHost(navController = navController, startDestination = startDestination) {
        composable(OnboardingDestination.route) {
            OnboardingScreen(navController)
        }
        composable(HomeDestination.route) {
            HomeScreen(navController)
        }
        composable(ProfileDestination.route) {
            ProfileScreen(navController)
        }
    }
}

