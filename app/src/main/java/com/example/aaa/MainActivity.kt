package com.example.aaa

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.aaa.ui.auth.ProfileScreen
import com.example.aaa.ui.auth.RegistrationScreen
import com.example.aaa.ui.gallery.AddScreen
import com.example.aaa.ui.gallery.GalleryScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MainScreen()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) {
        // Переключаем экран в зависимости от выбранной вкладки
        when (selectedItem) {
            0 -> MapScreen()
            1 -> AddScreen()
            2 -> GalleryScreen()
            3 -> {
                if (currentUser != null) {
                    // Если пользователь авторизован, передаем его email в ProfileScreen
                    ProfileScreen(
                        email = currentUser!!.email ?: "Unknown User",
                        onSignOut = {
                            FirebaseAuth.getInstance().signOut()
                            // После выхода меняем состояние, чтобы вернуться на экран регистрации
                            currentUser = null
                        }
                    )
                } else {
                    // Если нет, показываем экран регистрации
                    RegistrationScreen()
                }
            }
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
            // Кнопка карты
            IconButton(onClick = { onItemSelected(0) }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Map",
                    modifier = iconModifier,
                    tint = if (selectedItem == 0) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Кнопка добавления нового воспоминания
            IconButton(onClick = { onItemSelected(1) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new memory",
                    modifier = iconModifier,
                    tint = if (selectedItem == 1) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Кнопка галереи
            IconButton(onClick = { onItemSelected(2) }) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "List of memories",
                    modifier = iconModifier,
                    tint = if (selectedItem == 2) Color(0xFFF6D8E4) else Color.White
                )
            }

            // Кнопка профиля
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDEDED))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Сообщение с текстом, не во всю ширину
            Text(
                text = "Извините, экран с картой ещё в разработке",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    color = Color(0xFF333333)
                ),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFFF6D8E4), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )

            Text(
                text = "🔧",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RegistrationScreen()
}