package com.example.aaa.ui.gallery

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.aaa.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.File
import java.io.FileOutputStream
import android.Manifest




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
    val memories = remember { mutableStateListOf<Memory>() }
    val firestore = Firebase.firestore

    // Загружаем данные из Firestore
    LaunchedEffect(Unit) {
        firestore.collection("memories")
            .get()
            .addOnSuccessListener { documents ->
                memories.clear()
                for (document in documents) {
                    val memory = document.toObject(Memory::class.java)
                    memories.add(memory)
                }
            }
            .addOnFailureListener {
                // Обработка ошибки
            }
    }

    if (memories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет воспоминаний", fontSize = 16.sp, color = Color.Gray)
        }
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 90.dp)
        ) {
            memories.forEach { memory ->
                MemoryCard(memory)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MemoryCard(memory: Memory) {
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Проверка разрешений перед копированием изображения
    LaunchedEffect(Unit) {
        checkPermissions(context)
    }

    // Загружаем изображение в локальное хранилище
    val localImageFile = memory.images.firstOrNull()?.let { uri ->
        copyUriToInternalStorage(context, Uri.parse(uri), memory.eventName)
    }


    val imagePainter = rememberAsyncImagePainter(
        model = localImageFile?.absolutePath
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Дата
        Text(
            text = memory.date,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Фото
        if (localImageFile != null && localImageFile.exists()) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет изображения", color = Color.Black, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Название события
        Text(
            text = memory.eventName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Местоположение
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.place),
                contentDescription = "Местоположение",
                tint = Color(0xFFED70A0),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = memory.location, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле с треком
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.music),
                contentDescription = "Трек",
                tint = Color(0xFFED70A0),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Трек", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = memory.track, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле с описанием
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                .padding(8.dp)
        ) {
            Text(
                text = if (isDescriptionExpanded) memory.description else memory.description.take(100) + "...",
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun checkPermissions(context: Context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100
        )
    }
}

fun copyUriToInternalStorage(context: Context, uri: Uri, uniqueIdentifier: String): File? {
    return try {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            return null
        }

        val fileName = "image_$uniqueIdentifier.jpg" // Уникальное имя файла
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        Log.d("CopyFile", "File copied to ${file.absolutePath}")
        file
    } catch (e: Exception) {
        Log.e("CopyFile", "Error copying file", e)
        null
    }
}
