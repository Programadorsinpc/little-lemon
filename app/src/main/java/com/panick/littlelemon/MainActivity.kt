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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        database = AppDatabase.getDatabase(this)
        setContent {
            LittleLemonTheme {
                MyNavigation()
            }
        }
    }

    private fun fetchMenuDataAndStoreInDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: HttpResponse = client.get("https://api.tu-servidor.com/menu")
                val menuNetworkData: MenuNetworkData = response.body()
                storeMenuDataInDb(menuNetworkData.menu)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun storeMenuDataInDb(menuItems: List<MenuItemNetwork>) {
        val menuItemEntities = menuItems.map { menuItem ->
            MenuItemEntity(
                id = menuItem.id,
                title = menuItem.title,
                description = menuItem.description,
                price = menuItem.price,
                image = menuItem.image
            )
        }
        database.menuItemDao().deleteAllMenuItems()
        database.menuItemDao().insertMenuItems(menuItemEntities)
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



