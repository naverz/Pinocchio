/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.compose.slider

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.*
import io.github.naverz.pinocchio.slider.compose.palette.SliderPalette
import io.github.naverz.pinocchio.slider.compose.palette.ThumbPalette
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke

@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun HueSlider(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 360.0)
    hue: Float = DEFAULT_HSV_HUE,
    thumb: @Composable () -> Unit,
    isVertical: Boolean = false,
    sliderWidth: Dp = defaultSliderWidth,
    sliderCornerRadius: Dp = defaultCornerRadius,
    sliderStroke: Stroke? = null,
    sliderElevation: Dp? = null,
    onHueChanged: ((hue: Float) -> Unit)? = null,
    onHueConfirmed: ((hue: Float) -> Unit)? = null,
) {

    val brush = remember(isVertical) {
        if (isVertical) {
            Brush.verticalGradient(
                listOf(
                    Color.hsv(360f, 1f, 1f),
                    Color.hsv(300f, 1f, 1f),
                    Color.hsv(240f, 1f, 1f),
                    Color.hsv(180f, 1f, 1f),
                    Color.hsv(120f, 1f, 1f),
                    Color.hsv(60f, 1f, 1f),
                    Color.hsv(0f, 1f, 1f),
                ), tileMode = TileMode.Repeated
            )
        } else {
            Brush.horizontalGradient(
                listOf(
                    Color.hsv(0f, 1f, 1f),
                    Color.hsv(60f, 1f, 1f),
                    Color.hsv(120f, 1f, 1f),
                    Color.hsv(180f, 1f, 1f),
                    Color.hsv(240f, 1f, 1f),
                    Color.hsv(300f, 1f, 1f),
                    Color.hsv(360f, 1f, 1f)
                ), tileMode = TileMode.Repeated
            )
        }
    }
    Slider(
        modifier = modifier,
        value = (hue / 360).coerceIn(0f, 1f),
        isVertical = isVertical,
        slider = {
            SliderPalette.NormalSlider(
                isVertical = isVertical,
                sliderWidth = sliderWidth,
                sliderBackground = Background.BrushShape(
                    brush, RoundedCornerShape(sliderCornerRadius)
                ),
                sliderStroke = sliderStroke,
                sliderElevation = sliderElevation
            )
        },
        thumb = thumb,
        onValueChanged = { value ->
            onHueChanged?.invoke(
                (value * 360).coerceIn(0f, 360f)
            )
        },
        onValueConfirmed = { value ->
            onHueConfirmed?.invoke(
                (value * 360).coerceIn(0f, 360f)
            )
        }
    )
}


@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun HueSlider(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 360.0)
    hue: Float = DEFAULT_HSV_HUE,
    isVertical: Boolean = false,
    thumbSize: DpSize = defaultThumbSize,
    thumbShape: Shape = CircleShape,
    thumbStroke: Stroke? = defaultThumbStroke,
    thumbBackgroundColor: Color? = null,
    thumbElevation: Dp? = null,
    sliderWidth: Dp = defaultSliderWidth,
    sliderCornerRadius: Dp = defaultCornerRadius,
    sliderStroke: Stroke? = null,
    sliderElevation: Dp? = null,
    onHueChanged: ((hue: Float) -> Unit)? = null,
    onHueConfirmed: ((hue: Float) -> Unit)? = null,
) {
    HueSlider(
        modifier = modifier,
        hue = hue,
        isVertical = isVertical,
        sliderWidth = sliderWidth,
        sliderCornerRadius = sliderCornerRadius,
        sliderStroke = sliderStroke,
        sliderElevation = sliderElevation,
        thumb = {
            ThumbPalette.Thumb(
                thumbBackgroundColor = thumbBackgroundColor
                    ?: Color.hsv(hue = hue, saturation = 1f, value = 1f),
                thumbSize = thumbSize,
                thumbStroke = thumbStroke,
                thumbShape = thumbShape,
                thumbElevation = thumbElevation,
            )
        },
        onHueChanged = onHueChanged,
        onHueConfirmed = onHueConfirmed
    )
}

@Preview
@Composable
fun PreviewHueSlider() {
    var hue by remember {
        mutableStateOf(0f)
    }
    Column {
        HueSlider(
            hue = hue,
            isVertical = false,
            thumbElevation = 4.dp,
            onHueChanged = { hue = it }
        )
        Spacer(modifier = Modifier.height(30.dp))
        HueSlider(
            Modifier.height(200.dp),
            hue = hue,
            isVertical = true,
            onHueChanged = { hue = it },
            thumbShape = RectangleShape,
            thumbSize = DpSize(
                (defaultThumbRadius + defaultThumbStrokeWidth) * 2,
                (defaultThumbRadius + defaultThumbStrokeWidth) / 2
            ),
            thumbElevation = 4.dp,
            sliderCornerRadius = 0.dp
        )
        Box(Modifier.fillMaxWidth()) {
            BasicText(
                modifier = Modifier.align(Alignment.Center), text = "Hue : $hue"
            )
        }
    }
}