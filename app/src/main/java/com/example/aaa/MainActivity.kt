package com.example.aaa

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aaa.ui.auth.RegistrationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) {
        // Здесь можно добавить содержимое для различных экранов
        when (selectedItem) {
            0 -> MapScreen()
            1 -> AddScreen()
            2 -> BookmarkScreen()
            3 -> RegistrationScreen() // переход к экрану регистрации
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp),
        containerColor = Color.Gray
    ) {
        val iconModifier = Modifier.size(30.dp)

        // Создаем горизонтальное распределение иконок
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button for "Map" icon
            IconButton(onClick = { onItemSelected(0) }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Map",
                    modifier = iconModifier,
                    tint = if (selectedItem == 0) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Button for "Add" icon
            IconButton(onClick = { onItemSelected(1) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new memory",
                    modifier = iconModifier,
                    tint = if (selectedItem == 1) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Button for "Bookmark" icon
            IconButton(onClick = { onItemSelected(2) }) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "List of memories",
                    modifier = iconModifier,
                    tint = if (selectedItem == 2) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Button for "Account" icon
            IconButton(onClick = { onItemSelected(3) }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Account",
                    modifier = iconModifier,
                    tint = if (selectedItem == 3) Color(0xFFF6D8E4) else Color.White
                )
            }
        }
    }
}

@Composable
fun MapScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Map Screen")
    }
}

@Composable
fun AddScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Add Screen")
    }
}

@Composable
fun BookmarkScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Bookmark Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}