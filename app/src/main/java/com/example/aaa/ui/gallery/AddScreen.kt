package com.example.aaa.ui.gallery

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aaa.R
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

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6E6E6))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Блок для добавления фото (формат 4:5)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f * 1.5f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)) // Обрезка содержимого по скругленной рамке
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Форма добавления информации
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)) // Обрезка содержимого по скругленной рамке
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

            // Добавление описания через диалоговое окно
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { isDescriptionDialogVisible = true }
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (description.isEmpty()) {
                        "Добавить описание"
                    } else {
                        description.lineSequence().first().let { line ->
                            if (line.length > 40) "${line.take(40)}..." else line
                        }
                    },
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

            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)

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

            // Добавить местоположение
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                        location = "Москва" // Пример: задайте логику для ввода местоположения
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.place),
                    contentDescription = "Добавить местоположение",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (location.isEmpty()) "Добавить местоположение" else location,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            // Добавить трек
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                        track = "Трек 1" // Пример: задайте логику для выбора трека
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.music),
                    contentDescription = "Добавить трек",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (track.isEmpty()) "Добавить трек" else track,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Кнопка "Готово"
        OutlinedButton(
            onClick = { /* TODO: Логика сохранения */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC7C7C7)
            )
        ) {
            Text(text = "Готово", fontSize = 16.sp, color = Color.Black)
        }
    }
}
