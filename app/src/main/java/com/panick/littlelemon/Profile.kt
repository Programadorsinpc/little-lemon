package com.panick.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {
    // Recuperar datos de SharedPreferences si están disponibles
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val savedFirstName = sharedPreferences.getString("first_name", "")
    val savedLastName = sharedPreferences.getString("last_name", "")
    val savedEmail = sharedPreferences.getString("email", "")


    Spacer(modifier = Modifier.height(24.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        Text(text = "Profile info")
        Spacer(modifier = Modifier.height(16.dp))
        if (savedFirstName != null) {
            Text(text = savedFirstName)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (savedLastName != null) {
            Text(text = savedLastName)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (savedEmail != null) {
            Text(text = savedEmail)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            sharedPreferences.edit().clear().apply()
            navController.navigate(OnboardingDestination.route)
        }) {
            Text(text = "Log Out")
        }
    }
}
