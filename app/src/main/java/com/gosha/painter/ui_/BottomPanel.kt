package com.gosha.painter.ui_

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Компонент нижней панели, содержащей выбор цвета и регулировку ширины линии
@Composable
fun BottomPanel(onClick: (Color) -> Unit, onLineWidthChange: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Компонент для отображения списка цветов
        ColorList { color ->
            onClick(color)
        }
        // Компонент для регулировки ширины линии
        CustomSlider { lineWidth ->
            onLineWidthChange(lineWidth)
        }
    }
}

// Компонент для отображения списка цветов в горизонтальном списке
@Composable
fun ColorList(onClick: (Color) -> Unit) {
    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.White,
        Color.Black,
        Color.Yellow,
        Color.Green,
        Color.Magenta
    )
    LazyRow(modifier = Modifier.padding(10.dp)) {
        items(colors) { color ->
            // Отображение цветного кружка, который можно выбрать
            Box(modifier = Modifier
                .padding(end = 10.dp)
                .clickable {
                    onClick(color)
                }
                .size(40.dp)
                .background(color, CircleShape)) {

            }
        }
    }
}

// Компонент для регулировки ширины линии с помощью слайдера
@Composable
fun CustomSlider(onChange: (Float) -> Unit) {
    // Переменная для хранения текущей позиции слайдера
    var position by remember {
        mutableStateOf(0.05f)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Текстовое представление текущей ширины линии
        Text(text = "Ширина текста: ${(position * 100).toInt()}")
        // Слайдер для изменения ширины линии
        Slider(value = position, onValueChange = {
            val tempPos = if (it > 0) it else 0.1f
            position = tempPos
            onChange(tempPos * 100)
        })
    }
}
