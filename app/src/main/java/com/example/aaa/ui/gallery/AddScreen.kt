package com.example.aaa.ui.gallery

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.aaa.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen() {
    var eventName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var track by remember { mutableStateOf("") }
    var isDescriptionDialogVisible by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var notificationMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris != null) {
            selectedImages = uris
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
            .padding(10.dp)
            .padding(bottom = 80.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Блок для добавления фото
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImages.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Добавление фотографий",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Добавьте фото", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImages.first()),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Установлено для заполнения
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Поля формы
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.White)
                .padding(5.dp)
        ) {
            // Название события
            TextField(
                value = eventName,
                onValueChange = { eventName = it },
                placeholder = { Text("Назовите событие", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(5.dp))

            // Описание
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isDescriptionDialogVisible = true }
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (description.isEmpty()) "Добавить описание" else description,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            if (isDescriptionDialogVisible) {
                AlertDialog(
                    onDismissRequest = { isDescriptionDialogVisible = false },
                    confirmButton = {
                        TextButton(onClick = { isDescriptionDialogVisible = false }) {
                            Text("OK")
                        }
                    },
                    text = {
                        Column {
                            Text("Введите описание", fontWeight = FontWeight.Bold)
                            TextField(
                                value = description,
                                onValueChange = { description = it },
                                placeholder = { Text("Добавьте описание...") }
                            )
                        }
                    }
                )
            }

            // Добавить дату
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                        val calendar = Calendar.getInstance()
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                date = String.format("%02d.%02d.%d", dayOfMonth, month + 1, year)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Добавить дату",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (date.isEmpty()) "Добавить дату" else date,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Местоположение
            TextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Добавьте местоположение", fontSize = 16.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            Divider(color = Color.LightGray, thickness = 1.dp)

            // Трек
            TextField(
                value = track,
                onValueChange = { track = it },
                placeholder = { Text("Добавьте трек", fontSize = 16.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Кнопка "Готово"
        OutlinedButton(
            onClick = {
                validateAndSave(
                    eventName, description, date, location, track, selectedImages,
                    onValidationFailed = { message -> notificationMessage = message },
                    onSaveSuccess = { notificationMessage = "Данные успешно сохранены!" }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC7C7C7))
        ) {
            Text(text = "Готово", fontSize = 16.sp, color = Color.Black)
        }

        notificationMessage?.let {
            Text(it, color = if (it == "Данные успешно сохранены!") Color.Green else Color.Red, modifier = Modifier.padding(top = 10.dp))
        }
    }
}

// Проверка данных и сохранение
fun validateAndSave(
    eventName: String,
    description: String,
    date: String,
    location: String,
    track: String,
    selectedImages: List<Uri>,
    onValidationFailed: (String) -> Unit,
    onSaveSuccess: () -> Unit
) {
    // Проверка на пустоту всех обязательных полей
    if (eventName.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty() || track.isEmpty() || selectedImages.isEmpty()) {
        onValidationFailed("Заполните все поля и добавьте фото.")
        return
    }

    // Если все поля заполнены, сохраняем данные в Firestore
    val memory = hashMapOf(
        "eventName" to eventName,
        "description" to description,
        "date" to date,
        "location" to location,
        "track" to track,
        "images" to selectedImages.map { it.toString() }
    )

    val firestore = Firebase.firestore
    firestore.collection("memories")
        .add(memory)
        .addOnSuccessListener { onSaveSuccess() }
        .addOnFailureListener { e -> onValidationFailed("Ошибка сохранения: ${e.message}") }
}