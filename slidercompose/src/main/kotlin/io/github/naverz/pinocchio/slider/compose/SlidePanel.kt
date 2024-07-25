/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose

import androidx.annotation.FloatRange
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.palette.PanelPalette
import io.github.naverz.pinocchio.slider.compose.palette.ThumbPalette

@Composable
fun SlidePanel(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    x: Float = DEFAULT_SLIDER_PANEL_X_VALUE,
    @FloatRange(from = 0.0, to = 1.0)
    y: Float = DEFAULT_SLIDER_PANEL_Y_VALUE,
    thumb: (@Composable () -> Unit),
    panel: (@Composable () -> Unit) = {
        PanelPalette.Panel(
            Background.ColorShape(
                Color.Red,
                RoundedCornerShape(defaultCornerRadius)
            )
        )
    },
    onValueChanged: ((x: Float, y: Float) -> Unit)? = null,
    onValueConfirmed: ((x: Float, y: Float) -> Unit)? = null,
) {
    var containerSize by remember { mutableStateOf(IntSize(0, 0)) }
    var thumbSize by remember { mutableStateOf(IntSize(0, 0)) }
    val updatedOnValueChanged by rememberUpdatedState(newValue = onValueChanged)
    val updatedOnValueConfirmed by rememberUpdatedState(newValue = onValueConfirmed)
    val isRtl = isRtl()
    Layout(contents = listOf(thumb, panel),
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown()
                val halfThumbWidth = thumbSize.width.toFloat() / 2
                val halfThumbHeight = thumbSize.height.toFloat() / 2
                val panelRect = Rect(
                    halfThumbWidth,
                    halfThumbHeight,
                    containerSize.width - halfThumbWidth,
                    containerSize.height - halfThumbHeight
                )
                var nextTouchPoint: Offset? = null
                do {
                    val event: PointerEvent = awaitPointerEvent()
                    event.changes.forEach { pointerInputChange: PointerInputChange ->
                        findNextValue(
                            isRtl = isRtl,
                            containerSize = containerSize,
                            panelRect = panelRect,
                            touchPoint = Offset(
                                x = pointerInputChange.position.x,
                                y = pointerInputChange.position.y
                            ),
                            halfThumbWidth = halfThumbWidth,
                            halfThumbHeight = halfThumbHeight
                        ).let {
                            nextTouchPoint = it
                            updatedOnValueChanged?.invoke(it.x, it.y)
                        }
                        if (pointerInputChange.positionChange() != Offset.Zero)
                            pointerInputChange.consume()
                    }
                } while (event.changes.any { it.pressed })
                nextTouchPoint?.let { stableNextTouchPoint ->
                    updatedOnValueConfirmed?.invoke(
                        stableNextTouchPoint.x, stableNextTouchPoint.y
                    )
                }
            }
        }) { (thumbMeasurable, panelMeasurable), constraints ->
        val fixedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val thumbPlaceable =
            thumbMeasurable
                .map { it.measure(fixedConstraints) }
                .first()
        thumbSize = IntSize(thumbPlaceable.width, thumbPlaceable.height)
        val panelPlaceable = panelMeasurable
            .map {
                val maxWidth = constraints.maxWidth - thumbPlaceable.width
                val maxHeight = constraints.maxHeight - thumbPlaceable.height
                it.measure(
                    constraints.copy(
                        minWidth = 0,
                        maxWidth = maxWidth,
                        minHeight = 0,
                        maxHeight = maxHeight
                    )
                )
            }
            .first()

        val wholeWidth = panelPlaceable.width + thumbPlaceable.width
        val wholeHeight = panelPlaceable.height + thumbPlaceable.height


        containerSize = IntSize(wholeWidth, wholeHeight)
        layout(wholeWidth, wholeHeight) {
            panelPlaceable.placeRelative(thumbPlaceable.width / 2, thumbPlaceable.height / 2)
            thumbPlaceable.placeRelative(
                (x * (wholeWidth - thumbPlaceable.width)).toInt(),
                ((1 - y) * (wholeHeight - thumbPlaceable.height)).toInt()
            )
        }
    }
}

private fun findNextValue(
    isRtl: Boolean,
    containerSize: IntSize,
    panelRect: Rect,
    touchPoint: Offset,
    halfThumbWidth: Float,
    halfThumbHeight: Float
): Offset {
    return when {
        touchPoint.x < panelRect.left -> {
            when {
                touchPoint.y < panelRect.top -> {
                    Offset(if (isRtl) 1f else 0f, 1f)
                }
                touchPoint.y > panelRect.bottom -> {
                    Offset(if (isRtl) 1f else 0f, 0f)
                }
                else -> {
                    val newY =
                        1 - (touchPoint.y - halfThumbHeight) / (containerSize.height - halfThumbHeight * 2)
                    Offset(
                        if (isRtl) 1f else 0f,
                        newY.coerceIn(0f, 1f)
                    )
                }
            }
        }
        touchPoint.x > panelRect.right -> {
            when {
                touchPoint.y < panelRect.top -> {
                    Offset(if (isRtl) 0f else 1f, 1f)
                }
                touchPoint.y > panelRect.bottom -> {
                    Offset(if (isRtl) 0f else 1f, 0f)
                }
                else -> {
                    val newY =
                        1 - (touchPoint.y - halfThumbHeight) / (containerSize.height - halfThumbHeight * 2)
                    Offset(
                        if (isRtl) 0f else 1f,
                        newY.coerceIn(0f, 1f)
                    )
                }
            }
        }
        touchPoint.y > panelRect.bottom -> {
            when {
                touchPoint.x < panelRect.left -> {
                    Offset(if (isRtl) 1f else 0f, 0f)
                }
                touchPoint.x > panelRect.right -> {
                    Offset(if (isRtl) 0f else 1f, 0f)
                }
                else -> {
                    val newX =
                        (touchPoint.x - halfThumbWidth) / (containerSize.width - halfThumbWidth * 2)
                    Offset(
                        if (isRtl) 1 - newX.coerceIn(0f, 1f) else newX.coerceIn(0f, 1f),
                        0f
                    )
                }
            }
        }
        touchPoint.y < panelRect.top -> {
            when {
                touchPoint.x < panelRect.left -> {
                    Offset(if (isRtl) 1f else 0f, 1f)
                }
                touchPoint.x > panelRect.right -> {
                    Offset(if (isRtl) 0f else 1f, 1f)
                }
                else -> {
                    val newX =
                        (touchPoint.x - halfThumbWidth) / (containerSize.width - halfThumbWidth * 2)
                    Offset(
                        if (isRtl) 1 - newX.coerceIn(0f, 1f) else newX.coerceIn(0f, 1f),
                        1f
                    )
                }
            }
        }
        else -> {
            val newX = (touchPoint.x - halfThumbWidth) / (containerSize.width - halfThumbWidth * 2)
            val newY =
                1 - (touchPoint.y - halfThumbHeight) / (containerSize.height - halfThumbHeight * 2)
            Offset(
                if (isRtl) 1 - newX.coerceIn(0f, 1f) else newX.coerceIn(0f, 1f),
                newY.coerceIn(0f, 1f)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSliderPanel() {
    var x by remember {
        mutableFloatStateOf(0f)
    }
    var y by remember {
        mutableFloatStateOf(0f)
    }
    Column {
        SlidePanel(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(20.dp),
            x = x, y = y,
            onValueChanged = { newX: Float, newY: Float ->
                x = newX
                y = newY
            },
            panel = {
                PanelPalette.Panel(
                    panelBackground = Background.ColorShape(
                        color = Color.Red,
                        shape = RoundedCornerShape(defaultCornerRadius)
                    ),
                    panelStroke = Stroke.WidthColor(defaultThumbStrokeWidth, Color.White),
                    elevation = 4f.dp
                )
            },
            thumb = {
                ThumbPalette.CircleThumb(
                    thumbRadius = defaultThumbRadius + defaultThumbStrokeWidth,
                    color = Color.Black,
                    thumbStroke = Stroke.WidthColor(defaultThumbStrokeWidth, Color.White),
                    thumbElevation = 4f.dp
                )
            }
        )
        Box(Modifier.fillMaxWidth()) {
            BasicText(
                modifier = Modifier.align(Alignment.Center), text = "x : $x, y: $y"
            )
        }
    }

}
