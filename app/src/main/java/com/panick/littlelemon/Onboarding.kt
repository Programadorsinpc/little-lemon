package com.panick.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Vamos a conocerte",
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserInputFields(
            firstName = firstName,
            onFirstNameChange = { firstName = it },
            lastName = lastName,
            onLastNameChange = { lastName = it },
            email = email,
            onEmailChange = { email = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        RegisterButton(
            firstName = firstName,
            lastName = lastName,
            email = email,
            context = context,
            navController = navController,
            onMessageChange = { message = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = if (message.contains("fallido")) Color.Red else Color.Green
        )
    }
}

@Composable
fun Header() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun UserInputFields(
    firstName: TextFieldValue,
    onFirstNameChange: (TextFieldValue) -> Unit,
    lastName: TextFieldValue,
    onLastNameChange: (TextFieldValue) -> Unit,
    email: TextFieldValue,
    onEmailChange: (TextFieldValue) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
    }
}

@Composable
fun RegisterButton(
    firstName: TextFieldValue,
    lastName: TextFieldValue,
    email: TextFieldValue,
    context: Context,
    navController: NavHostController,
    onMessageChange: (String) -> Unit
) {
    Button(
        onClick = {
            if (firstName.text.isBlank() || lastName.text.isBlank() || email.text.isBlank()) {
                onMessageChange("Registro fallido. Por favor, introduzca todos los datos")
            } else {
                saveUserData(context, firstName.text, lastName.text, email.text)
                onMessageChange("¡Registro realizado con éxito!")
                navController.navigate(HomeDestination.route)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Registrarse")
    }
}

fun saveUserData(context: Context, firstName: String, lastName: String, email: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putString("first_name", firstName)
        putString("last_name", lastName)
        putString("email", email)
        putBoolean("is_logged_in", true)
        apply()
    }
}
