package com.example.aaa.ui.gallery

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aaa.R

@Composable
fun GalleryScreen() {
    // Пример данных
    val eventDate = "22.07.24"
    val eventName = "Название события"
    val location = "Местоположение"
    val trackName = "Luverance"
    val description = "Море да мы были там и красивое солнце оранжевый закат прямо в горизонт уходящее вайбик невероятный и я эту картинку скачророро по  гпппп гпмпщ нгпгнп"

    // Состояние для развертывания описания
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 90.dp) // Отступ для предотвращения перекрытия с навигационным баром
        ) {
            // Дата
            Text(
                text = eventDate,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)

            )

            // Поле для изображений
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f * 1.5f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "здесь должна быть картинка",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Название события
            Text(
                text = eventName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Местоположение
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.place), // Замените на ваш ресурс
                    contentDescription = "Местоположение",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = location, fontSize = 16.sp)
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
                    painter = painterResource(id = R.drawable.music), // Замените на ваш ресурс
                    contentDescription = "Трек",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Трек", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = trackName, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.music2), // Замените на ваш ресурс
                    contentDescription = "Воспроизвести",
                    tint = Color(0xFFED70A0),
                    modifier = Modifier.size(32.dp)
                )
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
                    text = if (isDescriptionExpanded) description else description.take(100) + "...",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кружочки с датами
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("03.01.24", "12.09.23", "24.11.24", "25.11.24").forEach { date ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFD0BFFF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = date, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}