package com.example.moviefiltersample.component.hexagonbtn

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Point(val x: Float, val y: Float)

@Composable
fun HexagonButton (
    modifier: Modifier,
    fillColor: Color = Color(0XFF4B0082),
    primaryColor: Color = Color(0XFFAF42FF),
    secondaryColor: Color = Color(0XFFC4B5F2),
    index: Int = 0,
    displayName: String? = null,
    onClick: (Int) -> Unit = {},
){
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(if (displayName == null ) "$index" else "$displayName"),
        style = TextStyle(fontSize = 16.sp) // Customize the text style as needed
    )
    val textSize = textLayoutResult.size
    val textWidth = textSize.width
    val textHeight = textSize.height

    Box(
        modifier = modifier.pointerInput(Unit){
            detectTapGestures { offset ->
                Log.d("HexagonButton", "Clicked happend")
                val clickPoint = Point(offset.x, offset.y)
                val canvasWidth = size.width
                val canvasHeight = size.height
                val radius = minOf(canvasWidth, canvasHeight) / 2

                val corners: MutableList<Point> = mutableListOf()

                for (i in 1..6) {
                    val angle = 2.0 * PI / 6 * i
                    val x = (canvasWidth / 2 + radius * cos(angle)).toFloat()
                    val y = (canvasHeight / 2 + radius * sin(angle)).toFloat()
                    corners += (Point(x, y))
                }

                if (isPointInHexagon(clickPoint, corners)) {
                    // Handle click inside hexagon
                    Log.d("HexagonButton", "Clicked inside hexagon with index: $index")
                    onClick(index)
                }
                else {
                    Log.d("HexagonButton", "Clicked outside hexagon with index: $index")
                }

            }
        },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = minOf(canvasWidth, canvasHeight) / 2

            val path = Path().apply {
                moveTo(
                    x = (canvasWidth / 2 + radius * cos(0.0)).toFloat(),
                    y = (canvasHeight / 2 + radius * sin(0.0)).toFloat()
                )
                for (i in 1..6) {
                    val angle = 2.0 * PI / 6 * i
                    val x = (canvasWidth / 2 + radius * cos(angle)).toFloat()
                    val y = (canvasHeight / 2 + radius * sin(angle)).toFloat()
                    lineTo(x = x, y = y )
                }
                close()
            }

            drawPath(
                path = path,
                color = fillColor,
                style = Fill,
                alpha = 0.5f
            )

            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 6f.dp.toPx()),
                alpha = 1f
            )

            drawPath(
                path = path,
                color = secondaryColor,
                style = Stroke(width = 4f.dp.toPx()),
                alpha = 1f
            )

            drawPath(
                path = path,
                color = Color.White,
                style = Stroke(width = 1f.dp.toPx()),
                alpha = 1f
            )

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset((size.width - textWidth) / 2,(size.height - textHeight) / 2),
                color = Color.White
            )
        }
    }
}

/**
 * Function to check if the clicked point is inside the Hexagon
 **/
fun isPointInHexagon(clickPoint: Point, corners: List<Point>): Boolean {
    // Check if the point is inside the hexagon using the ray-casting algorithm
    var inside = false
    for (i in corners.indices) {
        val j = (i + 1) % 6
        val xi = corners[i].x
        val yi = corners[i].y
        val xj = corners[j].x
        val yj = corners[j].y

        val intersect = ((yi > clickPoint.y) != (yj > clickPoint.y)) &&
                (clickPoint.x < (xj - xi) * (clickPoint.y - yi) / (yj - yi) + xi)
        if (intersect) inside = !inside
    }

    return inside
}

@Preview
@Composable
fun HexagonButtonPreview (){
    HexagonButton(
        modifier = Modifier.height(200f.dp).width(200f.dp)
    )
}