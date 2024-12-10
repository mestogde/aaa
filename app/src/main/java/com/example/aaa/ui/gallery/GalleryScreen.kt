package com.example.aaa.ui.gallery

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.aaa.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Memory(
    val eventName: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val track: String = "",
    val images: List<String> = emptyList()
)

@Composable
fun GalleryScreen() {
    val firestore = Firebase.firestore
    var memories by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        firestore.collection("memories")
            .get()
            .addOnSuccessListener { result ->
                memories = result.documents.mapNotNull { it.data }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (memories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет воспоминаний", fontSize = 16.sp)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            memories.forEach { memory ->
                val eventName = memory["eventName"] as? String ?: "Без названия"
                val date = memory["date"] as? String ?: "Не указано"
                val description = memory["description"] as? String ?: "Описание отсутствует"
                val location = memory["location"] as? String ?: "Не указано"
                val track = memory["track"] as? String ?: "Не указано"
                val imageUrls = memory["images"] as? List<String> ?: emptyList()

                MemoryCard(
                    eventName = eventName,
                    date = date,
                    description = description,
                    location = location,
                    track = track,
                    imageUrls = imageUrls
                )
            }
        }
    }
}

@Composable
fun MemoryCard(
    eventName: String,
    date: String,
    description: String,
    location: String,
    track: String,
    imageUrls: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = date,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (imageUrls.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrls.first()),
                contentDescription = "Изображение",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = eventName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Местоположение: $location",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Трек: $track",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = description,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}