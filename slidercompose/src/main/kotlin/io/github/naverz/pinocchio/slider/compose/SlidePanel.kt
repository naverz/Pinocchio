/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose

import androidx.annotation.FloatRange
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
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
    SubcomposeLayout(
        modifier.pointerInput(onValueChanged, onValueConfirmed) {
            forEachGesture {
                awaitPointerEventScope {
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
                                onValueChanged?.invoke(it.x, it.y)
                            }
                            pointerInputChange.consumePositionChange()
                        }
                    } while (event.changes.any { it.pressed })
                    nextTouchPoint?.let { stableNextTouchPoint ->
                        onValueConfirmed?.invoke(stableNextTouchPoint.x, stableNextTouchPoint.y)
                    }
                }
            }
        }

    ) { constraints ->
        val fixedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val thumbComposable: @Composable () -> Unit = { thumb.invoke() }
        val panelComposableWithPadding: @Composable (thumbSize: IntSize) -> Unit = {
            val paddingValues = PaddingValues((it.width / 2).toDp(), (it.height / 2).toDp())
            Box(Modifier.padding(paddingValues)) { panel.invoke() }
        }
        val thumbPlaceable =
            subcompose("Thumb", content = thumbComposable)
                .map { it.measure(fixedConstraints) }
                .first()
        thumbSize = IntSize(thumbPlaceable.width, thumbPlaceable.height)
        val panelWithPaddingPlaceable =
            subcompose("Panel") { panelComposableWithPadding.invoke(thumbSize) }
                .map { it.measure(fixedConstraints) }.first()
        containerSize = IntSize(panelWithPaddingPlaceable.width, panelWithPaddingPlaceable.height)
        layout(panelWithPaddingPlaceable.width, panelWithPaddingPlaceable.height) {
            panelWithPaddingPlaceable.placeRelative(0, 0)
            thumbPlaceable.placeRelative(
                (x * (panelWithPaddingPlaceable.width - thumbPlaceable.width)).toInt(),
                ((1 - y) * (panelWithPaddingPlaceable.height - thumbPlaceable.height)).toInt()
            )
        }
    }
}

private fun findNextValue(
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
                    Offset(0f, 1f)
                }
                touchPoint.y > panelRect.bottom -> {
                    Offset(0f, 0f)
                }
                else -> {
                    val newY =
                        1 - (touchPoint.y - halfThumbHeight) / (containerSize.height - halfThumbHeight * 2)
                    Offset(
                        0f,
                        newY.coerceIn(0f, 1f)
                    )
                }
            }
        }
        touchPoint.x > panelRect.right -> {
            when {
                touchPoint.y < panelRect.top -> {
                    Offset(1f, 1f)
                }
                touchPoint.y > panelRect.bottom -> {
                    Offset(1f, 0f)
                }
                else -> {
                    val newY =
                        1 - (touchPoint.y - halfThumbHeight) / (containerSize.height - halfThumbHeight * 2)
                    Offset(
                        1f,
                        newY.coerceIn(0f, 1f)
                    )
                }
            }
        }
        touchPoint.y > panelRect.bottom -> {
            when {
                touchPoint.x < panelRect.left -> {
                    Offset(0f, 0f)
                }
                touchPoint.x > panelRect.right -> {
                    Offset(1f, 0f)
                }
                else -> {
                    val newX =
                        (touchPoint.x - halfThumbWidth) / (containerSize.width - halfThumbWidth * 2)
                    Offset(
                        newX.coerceIn(0f, 1f),
                        0f
                    )
                }
            }
        }
        touchPoint.y < panelRect.top -> {
            when {
                touchPoint.x < panelRect.left -> {
                    Offset(0f, 1f)
                }
                touchPoint.x > panelRect.right -> {
                    Offset(1f, 1f)
                }
                else -> {
                    val newX =
                        (touchPoint.x - halfThumbWidth) / (containerSize.width - halfThumbWidth * 2)
                    Offset(
                        newX.coerceIn(0f, 1f),
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
                newX.coerceIn(0f, 1f),
                newY.coerceIn(0f, 1f)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewSliderPanel() {
    var x by remember {
        mutableStateOf(0f)
    }
    var y by remember {
        mutableStateOf(0f)
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
