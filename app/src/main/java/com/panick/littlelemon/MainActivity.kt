package com.panick.littlelemon

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        lifecycleScope.launch {
            fetchMenuDataAndStoreInDb()
        }
        setContent {
            LittleLemonTheme {
                MyNavigation()
                /*val itemsMenu by database.menuItemDao().getAllMenuItems().observeAsState(emptyList())
                println("Datos en la DB: $itemsMenu")*/
            }
        }
    }

    private suspend fun fetchMenuDataAndStoreInDb() {
        try {
            val response: HttpResponse =
                client.get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            val menuNetworkData: MenuNetworkData = response.body()
            println(menuNetworkData)
            storeMenuDataInDb(menuNetworkData.menu)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun storeMenuDataInDb(menuItems: List<MenuItemNetwork>) {
        val menuItemEntities = menuItems.map { menuItem ->
            MenuItemEntity(
                id = menuItem.id,
                title = menuItem.title,
                description = menuItem.description,
                price = menuItem.price,
                image = menuItem.image,
                category = menuItem.category
            )
        }
        runBlocking(IO) {
            database.menuItemDao().deleteAllMenuItems()
            database.menuItemDao().insertMenuItems(menuItemEntities)
            println("Se ha guardado los datos")
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



