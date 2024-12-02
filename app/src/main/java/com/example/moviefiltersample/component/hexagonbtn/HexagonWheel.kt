package com.example.moviefiltersample.component.hexagonbtn

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.Collections
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin


enum class WheelType {
    Keyword, Year, Country, Sort, Genre, Adult, None
}

@Composable
fun HexagonWheel(
    modifier: Modifier,
    onClick: (WheelType) -> Unit = {},
    onCenterClick: (Int) -> Unit = {},
    onAnimationEnd: (WheelType) -> Unit = {}
){
    var center by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableIntStateOf(0) }
    val pointList = rememberSaveable { mutableStateOf(listOf<Offset>()) }
    val hexagonPoints = rememberSaveable {
        List(6) { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    }
    var lastClickedIndex by rememberSaveable { mutableIntStateOf(1) }
    var clickedIndex by rememberSaveable { mutableIntStateOf(1) }
    var animateState by rememberSaveable { mutableStateOf(AnimateState.Done) }
    val animateTime = 500L
    val animateScope = rememberCoroutineScope()

    val onItemClicked: (Int) -> Unit = { index ->
        if (animateState != AnimateState.Playing && index != clickedIndex) {
            animateState = AnimateState.Playing
            //record click index
            lastClickedIndex = clickedIndex
            clickedIndex = index

            var rotateVal= min(abs(index - lastClickedIndex), 6 - abs(index - lastClickedIndex))

            if (center.x - pointList.value[index-1].x > 0) {
                rotateVal = -rotateVal
            }
            pointList.value = pointList.value.toMutableList().also {
                Collections.rotate(it, rotateVal)
            }

            animateScope.launch {
                hexagonPoints.forEachIndexed { i, point ->
                    launch {
                        val loopTime = abs(rotateVal)
                        for (rt in loopTime - 1 downTo 0) {
                            Log.e("HexagonWheel", "rt: $rt")
                            val rtVal = if (rotateVal > 0) -rt else rt
                            val lastPointState = pointList.value.toMutableList().also {
                                Collections.rotate(it, rtVal)
                            }

                            val rotateUpDegree = getAngleDegree(center, lastPointState[i]) + if (rotateVal > 0) 30 else -30
                            val rotateUpPoint = calSolarPoint(
                                center.x,
                                center.y,
                                rotateUpDegree.toDouble(),
                                size/2.75
                            )

                            point.animateTo(
                                targetValue = rotateUpPoint,
                                animationSpec = tween(250, easing = FastOutSlowInEasing)
                            )
                        }

                        point.animateTo(
                            targetValue = pointList.value[i],
                            animationSpec = tween(250, easing = FastOutSlowInEasing)
                        )

                        animateState = AnimateState.Done
                        onAnimationEnd(WheelType.entries[clickedIndex - 1])
                    }
                }
            }
            onClick(WheelType.entries[index])
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                size = min(coordinates.size.width, coordinates.size.height)
                center = Offset(
                    coordinates.size.width / 2f,
                    coordinates.size.height / 2f
                )
            }
    ) {
        ///index 0 will be the center point, index 1 will be the top point
        val hexagonBtnSize = with(LocalDensity.current) { (size/3 - 1.dp.toPx()).toDp() }

        LaunchedEffect(Unit) {
            if (pointList.value.isEmpty()){
                val hexagonDis = 2.5 * size / 8
                val pointListTemp = mutableListOf<Offset>()
                for (i in 1..6){
                    pointListTemp += calSolarPoint(center.x, center.y, ((i-1)*60 + 270).toDouble(), hexagonDis)
                }
                pointList.value = pointListTemp

                hexagonPoints.forEachIndexed { i, point ->
                    point.snapTo(center)
                    launch {
                        delay(Duration.ofMillis(100L))
                        point.animateTo(
                            targetValue = pointList.value[i],
                            animationSpec = tween(durationMillis = animateTime.toInt(), easing = LinearEasing)
                        )
                    }
                }
            }
        }

        for (i in 1..6){
            CenteredHexagonButton(
                hexagonBtnSize = hexagonBtnSize,
                targetPoint = hexagonPoints[i - 1].value,
                currentIndex = i,
                onClick = onItemClicked
            )
        }

        CenteredHexagonButton(
            hexagonBtnSize = hexagonBtnSize,
            targetPoint = center,
            name = "GO!",
            onClick = onCenterClick
        )
    }
}

@Composable
fun CenteredHexagonButton(
    hexagonBtnSize: Dp,
    targetPoint: Offset,
    currentIndex: Int = 0,
    name: String = "",
    onClick: (Int) -> Unit = {}
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .onSizeChanged { size = it }
            .offset {
                IntOffset(
                    (targetPoint.x - size.width / 2).roundToInt(),
                    (targetPoint.y - size.height / 2).roundToInt()
                )
            }
    ) {
        HexagonButton(
            modifier = Modifier
                .width(hexagonBtnSize)
                .height(hexagonBtnSize),
            index = currentIndex,
            displayName = if (currentIndex > 0) WheelType.entries[currentIndex - 1].name else name,
            onClick = onClick
        )
    }
}

@Preview
@Composable
fun HexagonWheelPreview (){
    HexagonWheel(
        modifier = Modifier
            .height(200f.dp)
            .width(200f.dp)
    )
}


fun calSolarPoint(originX: Float, originY: Float, angleDegrees: Double, distance: Double): Offset {
    val radians = angleDegrees * (PI / 180)
    val xOffset = distance * cos(radians)
    val yOffset = distance * sin(radians)
    return Offset((originX + xOffset).toFloat(), (originY + yOffset).toFloat())
}

fun getAngleDegree(pointA: Offset, pointB: Offset): Float {
    val angleRadians= atan2(pointB.y - pointA.y, pointB.x - pointA.x)
    return radiansToDegrees(angleRadians.toDouble()).toFloat()
}

fun radiansToDegrees(radians: Double): Double {
    return radians * (180.0 / Math.PI)
}

enum class AnimateState {
    Playing, Done
}