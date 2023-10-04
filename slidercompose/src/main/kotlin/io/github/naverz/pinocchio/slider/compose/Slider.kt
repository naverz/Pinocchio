/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.palette.SliderPalette
import io.github.naverz.pinocchio.slider.compose.palette.ThumbPalette
import io.github.naverz.pinocchio.slider.compose.palette.property.NarrowSliderProperty

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Slider(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    value: Float = DEFAULT_SLIDER_VALUE,
    thumb: (@Composable () -> Unit),
    isVertical: Boolean = false,
    slider: (@Composable () -> Unit),
    onValueChanged: ((value: Float) -> Unit)? = null,
    onValueConfirmed: ((value: Float) -> Unit)? = null,
) {
    var containerSize by remember { mutableStateOf(IntSize(0, 0)) }
    var thumbSize by remember { mutableStateOf(IntSize(0, 0)) }
    val updatedOnValueChanged by rememberUpdatedState(newValue = onValueChanged)
    val updatedOnValueConfirmed by rememberUpdatedState(newValue = onValueConfirmed)
    val isRtl = isRtl()
    Box(modifier) {
        Layout(
            modifier = if (isVertical) {
                Modifier.fillMaxHeight()
            } else {
                Modifier.fillMaxWidth()
            }
                .pointerInput(isVertical) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitFirstDown()
                            var nextValue: Float? = null
                            do {
                                val event: PointerEvent = awaitPointerEvent()
                                event.changes.forEach { pointerInputChange: PointerInputChange ->
                                    findNextValue(
                                        isRtl = isRtl,
                                        isVertical = isVertical,
                                        touchOffset = if (isVertical) pointerInputChange.position.y else pointerInputChange.position.x,
                                        maxOffset = (if (isVertical) containerSize.height else containerSize.width).toFloat(),
                                        thumbStandardLength = (if (isVertical) thumbSize.height else thumbSize.width).toFloat()
                                    ).let {
                                        nextValue = it
                                        updatedOnValueChanged?.invoke(it)
                                    }
                                    pointerInputChange.consumePositionChange()
                                }
                            } while (event.changes.any { it.pressed })
                            nextValue?.let { stableNextValue ->
                                updatedOnValueConfirmed?.invoke(stableNextValue)
                            }
                        }
                    }
                }
                .align(Alignment.Center),
            contents = listOf(thumb, slider)
        ) { (thumbMeasurable, sliderMeasurable), constraints ->
            val fixedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            val thumbPlaceable =
                thumbMeasurable
                    .map { it.measure(fixedConstraints) }
                    .first()
            thumbSize = IntSize(thumbPlaceable.width, thumbPlaceable.height)

            val sliderPlaceable = sliderMeasurable
                .map {
                    it.measure(
                        if (isVertical)
                            fixedConstraints.copy(maxHeight = fixedConstraints.maxHeight - thumbPlaceable.height)
                        else
                            fixedConstraints.copy(maxWidth = fixedConstraints.maxWidth - thumbPlaceable.width)
                    )
                }
                .first()

            val isThumbSmallerThenSlider =
                if (isVertical) sliderPlaceable.width > thumbPlaceable.width
                else sliderPlaceable.height > thumbPlaceable.height

            val containerWidth = if (isVertical) {
                if (isThumbSmallerThenSlider) {
                    sliderPlaceable.width
                } else {
                    thumbPlaceable.width
                }
            } else {
                fixedConstraints.maxWidth
            }
            val containerHeight = if (isVertical) {
                fixedConstraints.maxHeight
            } else {
                if (isThumbSmallerThenSlider) {
                    sliderPlaceable.height
                } else {
                    thumbPlaceable.height
                }
            }
            containerSize =
                IntSize(width = containerWidth, height = containerHeight)
            layout(containerWidth, containerHeight) {
                if (isVertical) {
                    sliderPlaceable.placeRelative(
                        x = if (isThumbSmallerThenSlider) {
                            0
                        } else {
                            (thumbPlaceable.width - sliderPlaceable.width) / 2
                        },
                        y = thumbPlaceable.height / 2
                    )
                } else {
                    sliderPlaceable.placeRelative(
                        x = thumbPlaceable.width / 2,
                        y = if (isThumbSmallerThenSlider) {
                            0
                        } else {
                            (thumbPlaceable.height - sliderPlaceable.height) / 2
                        }
                    )
                }

                if (isThumbSmallerThenSlider) {
                    thumbPlaceable.placeRelative(
                        x = if (isVertical)
                            (sliderPlaceable.width - thumbPlaceable.width) / 2
                        else
                            (value * sliderPlaceable.width).toInt(),
                        y = if (isVertical)
                            ((1 - value) * sliderPlaceable.height).toInt()
                        else
                            (sliderPlaceable.height - thumbPlaceable.height) / 2
                    )
                } else {
                    thumbPlaceable.placeRelative(
                        x = if (isVertical) 0 else (value * sliderPlaceable.width).toInt(),
                        y = if (isVertical) ((1 - value) * sliderPlaceable.height).toInt() else 0
                    )
                }
            }
        }
    }
}


private fun findNextValue(
    isRtl: Boolean,
    isVertical: Boolean,
    touchOffset: Float,
    maxOffset: Float,
    thumbStandardLength: Float
): Float {
    val halfOfThumbStandardLengthPx = thumbStandardLength / 2
    return if (touchOffset < halfOfThumbStandardLengthPx) {
        if (isVertical || isRtl) 1f else 0f
    } else if (touchOffset > maxOffset - halfOfThumbStandardLengthPx) {
        if (isVertical || isRtl) 0f else 1f
    } else {
        ((touchOffset - halfOfThumbStandardLengthPx) / (maxOffset - thumbStandardLength))
            .let { calculatedValue ->
                if (isVertical || isRtl) 1 - calculatedValue
                else calculatedValue
            }
            .coerceIn(0f, 1f)
    }
}

@Preview
@Composable
private fun PreviewSlider() {
    var value by remember {
        mutableStateOf(0f)
    }
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = value,
            slider = {
                SliderPalette.ActiveSlider(
                    activeValue = value,
                    sliderWidth = defaultSliderWidth,
                    isVertical = false,
                    activeBrush = SolidColor(Color.Gray),
                    inactivateBrush = SolidColor(Color.Red),
                    sliderCornerShape = RoundedCornerShape(defaultCornerRadius),
                    sliderStroke = Stroke.WidthColor(3.dp, Color.White),
                )
            },
            thumb = {
                ThumbPalette.CircleThumb(
                    thumbRadius = defaultThumbRadius + defaultThumbStrokeWidth,
                    color = Color.Black,
                    thumbStroke = Stroke.WidthColor(defaultThumbStrokeWidth, Color.White),
                    thumbElevation = 4f.dp
                )
            },
            onValueChanged = { value = it },
        )
        Spacer(modifier = Modifier.height(10.dp))
        Slider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = value,
            slider = {
                SliderPalette.ActiveSlider(
                    activeValue = value,
                    sliderWidth = 2.dp,
                    isVertical = false,
                    activeBrush = SolidColor(Color(0xff292930)),
                    inactivateBrush = SolidColor(Color(0xffE0E0E1)),
                    sliderCornerShape = RoundedCornerShape(4.dp),
                )
            },
            isVertical = false,
            thumb = {
                ThumbPalette.CircleThumb(
                    thumbRadius = 8.dp,
                    color = Color.White,
                    thumbElevation = 4f.dp
                )
            },
            onValueChanged = { value = it },
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Slider(
                Modifier
                    .height(200.dp)
                    .padding(horizontal = 10.dp),
                value = value,
                slider = {
                    SliderPalette.ActiveSlider(
                        activeValue = value,
                        sliderWidth = 2.dp,
                        isVertical = true,
                        activeBrush = SolidColor(Color(0xff292930)),
                        inactivateBrush = SolidColor(Color(0xffE0E0E1)),
                        sliderCornerShape = RoundedCornerShape(4.dp),
                    )
                },
                isVertical = true,
                thumb = {
                    ThumbPalette.CircleThumb(
                        thumbRadius = 8.dp,
                        color = Color.White,
                        thumbElevation = 4f.dp
                    )
                },
                onValueChanged = { value = it },
            )
            Spacer(modifier = Modifier.width(10.dp))
            Slider(
                Modifier.height(200.dp),
                value = value,
                isVertical = true,
                onValueChanged = { value = it },
                slider = {
                    SliderPalette.NormalSlider(
                        isVertical = true,
                        sliderWidth = defaultSliderWidth,
                        sliderBackground = Background.ColorShape(Color.Red, RectangleShape),
                        sliderStroke = Stroke.WidthColor(3.dp, Color.White),
                        sliderElevation = 4.dp
                    )
                },
                thumb = {
                    ThumbPalette.RectThumb(
                        thumbSize = DpSize(
                            (defaultThumbRadius + defaultThumbStrokeWidth) * 2,
                            (defaultThumbRadius + defaultThumbStrokeWidth) / 2
                        ),
                        color = Color.Black,
                        thumbStroke = defaultThumbStroke
                    )
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Slider(
                Modifier.height(200.dp),
                value = value,
                isVertical = true,
                onValueChanged = { value = it },
                slider = {
                    SliderPalette.NormalSlider(
                        isVertical = true,
                        sliderWidth = defaultSliderWidth,
                        sliderBackground = Background.ColorShape(Color.Red, RectangleShape),
                        sliderStroke = Stroke.WidthColor(3.dp, Color.White),
                        sliderElevation = 4.dp
                    )
                },
                thumb = {
                    ThumbPalette.RectThumb(
                        thumbSize = DpSize(
                            (defaultThumbRadius + defaultThumbStrokeWidth) / 2,
                            (defaultThumbRadius + defaultThumbStrokeWidth) / 2
                        ),
                        color = Color.Black,
                        thumbStroke = defaultThumbStroke
                    )
                }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Slider(
                Modifier.height(200.dp),
                value = value,
                isVertical = true,
                onValueChanged = { value = it },
                slider = {
                    SliderPalette.NarrowSlider(
                        isVertical = true,
                        sliderWidth = defaultSliderWidth,
                        sliderHeight = 200.dp,
                        property = NarrowSliderProperty(
                            startPeekWidth = 30.dp,
                            endPeekWidth = 15.dp
                        ),
                    )
                },
                thumb = {
                    ThumbPalette.CircleThumb(
                        thumbRadius = 18.dp,
                        color = Color.White,
                        thumbElevation = 4f.dp
                    )
                }
            )
        }

        Box(Modifier.fillMaxWidth()) {
            BasicText(
                modifier = Modifier.align(Alignment.Center), text = "Value : $value"
            )
        }
    }

}

@Preview
@Composable
private fun PreviewBalancingSlider() {
    var value by remember { mutableStateOf(0.5f) }
    Column {
        Spacer(modifier = Modifier.height(15.dp))
        Slider(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            value = value,
            slider = {
                SliderPalette.BalancingSlider(
                    value = value,
                    sliderWidth = 2.dp,
                    isVertical = false,
                    activeBrush = SolidColor(Color(0xff292930)),
                    inactivateBrush = SolidColor(Color(0xffE0E0E1)),
                    sliderCornerShape = RoundedCornerShape(4.dp),
                )
            },
            isVertical = false,
            thumb = {
                ThumbPalette.CircleThumb(
                    thumbRadius = 8.dp,
                    color = Color.White,
                    thumbElevation = 4f.dp
                )
            },
            onValueChanged = { value = it },
        )
        Slider(
            Modifier
                .height(150.dp)
                .padding(horizontal = 10.dp),
            value = value,
            slider = {
                SliderPalette.BalancingSlider(
                    value = value,
                    sliderWidth = 2.dp,
                    isVertical = true,
                    activeBrush = SolidColor(Color(0xff292930)),
                    inactivateBrush = SolidColor(Color(0xffE0E0E1)),
                    sliderCornerShape = RoundedCornerShape(4.dp),
                )
            },
            isVertical = true,
            thumb = {
                ThumbPalette.CircleThumb(
                    thumbRadius = 8.dp,
                    color = Color.White,
                    thumbElevation = 4f.dp
                )
            },
            onValueChanged = { value = it },
        )
        Box(Modifier.fillMaxWidth()) {
            BasicText(
                modifier = Modifier.align(Alignment.Center), text = "Value : $value"
            )
        }
    }
}

@Preview
@Composable
private fun PreviewStateTest() {
    var tagWithValue by remember { mutableStateOf(mapOf("a" to 0f, "b" to 0f, "c" to 0f)) }
    var tags by remember { mutableStateOf(listOf("a", "b", "c")) }

    Column(Modifier.fillMaxWidth()) {
        tags.forEach { tag ->
            Slider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                value = tagWithValue[tag] ?: 0f,
                slider = {
                    SliderPalette.BalancingSlider(
                        value = tagWithValue[tag] ?: 0f,
                        sliderWidth = 2.dp,
                        isVertical = false,
                        activeBrush = SolidColor(Color(0xff292930)),
                        inactivateBrush = SolidColor(Color(0xffE0E0E1)),
                        sliderCornerShape = RoundedCornerShape(4.dp),
                    )
                },
                isVertical = false,
                thumb = {
                    ThumbPalette.CircleThumb(
                        thumbRadius = 8.dp,
                        color = Color.White,
                        thumbElevation = 4f.dp
                    )
                },
                onValueChanged = {
                    tagWithValue = tagWithValue.toMutableMap()
                        .apply { this[tag] = it }
                },
            )
            BasicText(text = "tag = $tag, value =${tagWithValue[tag]}")
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        BasicText(text = "currentTags : $tags\ntagWithValue = $tagWithValue")
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            Modifier
                .padding(top = 20.dp)
                .background(Color.Red, RoundedCornerShape(4.dp))
                .align(CenterHorizontally)
                .clickable {
                    val newTags = if ("a" in tags) listOf("d", "e", "f") else listOf("a", "b", "c")
                    tags = newTags
                    tagWithValue = newTags.associateWith { 0f }
                }
                .padding(10.dp)
        ) {
            BasicText(text = "Change the tags states!")
        }
    }
}