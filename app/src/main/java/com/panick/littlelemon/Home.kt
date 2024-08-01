package com.panick.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun HomeScreen(navController: NavHostController) {
    // Obtener la base de datos
    val database = AppDatabase.getDatabase(LocalContext.current)

    // Observar los cambios en la lista de items del menú desde la base de datos
    val itemsMenu by database.menuItemDao().getAllMenuItems().observeAsState(emptyList())

    // Estado del campo de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtrar y ordenar los elementos del menú según la búsqueda
    val filteredMenu = itemsMenu
        .filter { item ->
            item.title.contains(searchQuery, ignoreCase = true) || // Filtra por título
                    item.description.contains(searchQuery, ignoreCase = true) // O por descripción
        }
        .sortedBy { it.title } // Ordenar alfabéticamente por título

    // Imagen de cabecera
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Fit
    )

    // Contenedor principal
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeroSection() // Sección de presentación

        // Campo de texto para búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar en el menú") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Ingresa una búsqueda") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp), // Estilizar el campo de texto
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espaciador

        // Lista de elementos del menú
        MenuItemsList(menu = filteredMenu)

        /*Spacer(modifier = Modifier.height(16.dp)) // Espaciador

        // Botón para navegar a la pantalla de perfil
        Button(
            onClick = {
                navController.navigate(ProfileDestination.route)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Order Take")
        }*/
    }
}

@Composable
fun HeroSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Little Lemon",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Chicago",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Somos un restaurante mediterráneo familiar, centrado en recetas tradicionales servidas con un toque moderno",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MenuItemsList(menu: List<MenuItemEntity>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        itemsIndexed(menu) { _, item ->
            MenuItem(itemMenu = item)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(itemMenu: MenuItemEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Acción al hacer clic en el elemento */ },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f) // Permitir que la columna tome todo el espacio disponible
            ) {
                // Título
                Text(
                    text = itemMenu.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                // Descripción
                Text(
                    text = itemMenu.description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                )
                // Precio
                Text(
                    text = "$ ${itemMenu.price}",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            GlideImage(
                model = itemMenu.image,
                contentDescription = "Imagen de ${itemMenu.title}",
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        color = Color.LightGray,
        thickness = 1.dp
    )
}

@Preview
@Composable
fun MenuItemPreview() {
    MenuItem(itemMenu = menuItems[0])
}

val menuItems = listOf(
    MenuItemEntity(
        id = "1",
        title = "Hamburguesa Clásica",
        description = "Jugosa hamburguesa con carne de res, lechuga, tomate y queso cheddar.",
        price = 8.99,
        image = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/pasta.jpg?raw=true",
        category = "Comida Rápida"
    ),
    MenuItemEntity(
        id = "2",
        title = "Pizza Margarita",
        description = "Deliciosa pizza con salsa de tomate, mozzarella fresca y albahaca.",
        price = 10.49,
        image = "https://example.com/images/pizza_margarita.jpg",
        category = "Italiana"
    ),
    MenuItemEntity(
        id = "3",
        title = "Ensalada César",
        description = "Ensalada fresca con lechuga romana, crutones, queso parmesano y aderezo César.",
        price = 7.50,
        image = "https://example.com/images/ensalada_cesar.jpg",
        category = "Ensaladas"
    ),
    MenuItemEntity(
        id = "4",
        title = "Sushi de Salmón",
        description = "Rollos de sushi rellenos de salmón fresco y aguacate, servidos con salsa de soja.",
        price = 12.99,
        image = "https://example.com/images/sushi_salmon.jpg",
        category = "Japonesa"
    ),
    MenuItemEntity(
        id = "5",
        title = "Tacos de Pollo",
        description = "Tortillas rellenas de pollo sazonado, cebolla, cilantro y salsa picante.",
        price = 6.75,
        image = "https://example.com/images/tacos_pollo.jpg",
        category = "Mexicana"
    ),
    MenuItemEntity(
        id = "6",
        title = "Pasta Carbonara",
        description = "Pasta italiana con salsa cremosa de huevo, queso parmesano, panceta y pimienta negra.",
        price = 9.50,
        image = "https://example.com/images/pasta_carbonara.jpg",
        category = "Italiana"
    ),
    MenuItemEntity(
        id = "7",
        title = "Sopa de Tomate",
        description = "Sopa caliente y reconfortante de tomate fresco con albahaca y crutones.",
        price = 5.99,
        image = "https://example.com/images/sopa_tomate.jpg",
        category = "Sopas"
    ),
    MenuItemEntity(
        id = "8",
        title = "Bistec a la Parrilla",
        description = "Bistec jugoso cocinado a la perfección con guarnición de papas fritas.",
        price = 15.99,
        image = "https://example.com/images/bistec_parrilla.jpg",
        category = "Carnes"
    ),
    MenuItemEntity(
        id = "9",
        title = "Helado de Vainilla",
        description = "Delicioso helado de vainilla cremoso, perfecto para un postre refrescante.",
        price = 3.50,
        image = "https://example.com/images/helado_vainilla.jpg",
        category = "Postres"
    ),
    MenuItemEntity(
        id = "10",
        title = "Café Espresso",
        description = "Fuerte y aromático café espresso preparado con granos de alta calidad.",
        price = 2.99,
        image = "https://example.com/images/cafe_espresso.jpg",
        category = "Bebidas"
    )
)
