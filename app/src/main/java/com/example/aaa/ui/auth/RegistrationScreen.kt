package com.example.aaa.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.example.aaa.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegistrationScreen() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentUser by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }

    // If the user is already logged in, show the profile screen
    if (currentUser != null) {
        ProfileScreen(currentUser!!.email ?: "Unknown User")
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Form container
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.7f)
                .align(Alignment.Center)
                .background(
                    color = Color.White.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Почта") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        Color.Gray, Color.Gray, Color.Black, Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        Color.Gray, Color.Gray, Color.Black, Color.Black
                    )
                )

                // Error message (if any)
                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // "Регистрация" button
                OutlinedButton(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            errorMessage = "Пожалуйста, заполните оба поля"
                        } else if (password.length < 6) {
                            errorMessage = "Слишком короткий пароль"
                        } else {
                            errorMessage = null
                            registerUser(email, password, context)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFF6D8E4),
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text(
                        text = "Регистрация",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "Вход" button
                OutlinedButton(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            errorMessage = "Пожалуйста, заполните оба поля"
                        } else {
                            signInUser(email, password, context) { user: FirebaseUser? ->
                                if (user != null) {
                                    currentUser = user
                                } else {
                                    errorMessage = "Сначала зарегистрируйтесь"
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Вход", fontSize = 16.sp)
                }
            }
        }
    }
}

// Функция регистрации
fun registerUser(email: String, password: String, context: android.content.Context) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Вы успешно зарегистрированы!", Toast.LENGTH_SHORT).show()
            } else {
                val exception = task.exception
                Toast.makeText(
                    context,
                    "Ошибка регистрации: ${exception?.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

// Функция входа
fun signInUser(
    email: String,
    password: String,
    context: android.content.Context,
    onSignIn: (FirebaseUser?) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignIn(auth.currentUser) // Передаём текущего пользователя
            } else {
                Toast.makeText(
                    context,
                    "Ошибка входа: ${task.exception?.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                onSignIn(null) // Если ошибка, передаём null
            }
        }
}

// Экран профиля
@Composable
fun ProfileScreen(email: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.background_profile_image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Profile content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🐱", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logout button
            OutlinedButton(
                onClick = { FirebaseAuth.getInstance().signOut() },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color.White)
            ) {
                Text("Выйти", fontSize = 16.sp)
            }
        }
    }
}
