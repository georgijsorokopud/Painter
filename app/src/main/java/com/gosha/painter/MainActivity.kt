package com.gosha.painter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.gosha.painter.ui.theme.PainterTheme
import com.gosha.painter.ui_.BottomPanel
import com.gosha.painter.ui_.PathData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Состояние для хранения текущих данных пути
            val pathData = remember {
                mutableStateOf(PathData())
            }
            PainterTheme {
                Column {
                    // Компонент для рисования на холсте
                    DrawCanvas(pathData)
                    // Нижняя панель для выбора цвета и толщины линии
                    BottomPanel(
                        { color ->
                            pathData.value = pathData.value.copy(
                                color = color
                            )
                        }){ lineWidth ->
                        pathData.value = pathData.value.copy(
                            lineWidth = lineWidth
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawCanvas(pathData: MutableState<PathData>) {
    // Временный путь для текущей линии
    var tempPath = Path()
    // Список всех нарисованных путей
    val pathList = remember {
        mutableStateListOf(PathData())
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f) // Холст занимает 75% высоты
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {
                        // Создание нового пути при начале рисования
                        tempPath = Path()
                    },
                    onDragEnd = {
                        // Добавление текущего пути в список после завершения рисования
                        pathList.add(
                            pathData.value.copy(
                                path = tempPath
                            )
                        )
                    }
                ) { change, dragAmount ->
                    // Обновление пути при перетаскивании
                    tempPath.moveTo(
                        change.position.x - dragAmount.x,
                        change.position.y - dragAmount.y
                    )
                    tempPath.lineTo(
                        change.position.x,
                        change.position.y
                    )

                    // Удаление предыдущего пути из списка и добавление обновленного пути
                    if (pathList.isNotEmpty()) {
                        pathList.removeAt(pathList.size - 1)
                    }
                    pathList.add(
                        pathData.value.copy(
                            path = tempPath
                        )
                    )
                }
            }
    ) {
        // Рисование всех путей на холсте
        pathList.forEach { pathData ->
            drawPath(
                pathData.path,
                color = pathData.color,
                style = Stroke(pathData.lineWidth, cap = StrokeCap.Round)
            )
        }
    }
}
