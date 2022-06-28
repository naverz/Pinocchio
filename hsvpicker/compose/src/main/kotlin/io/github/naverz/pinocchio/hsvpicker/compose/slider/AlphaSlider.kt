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

@Composable
fun AlphaSlider(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = DEFAULT_ALPHA,
    thumb: @Composable () -> Unit,
    isVertical: Boolean = false,
    sliderWidth: Dp = defaultSliderWidth,
    sliderCornerRadius: Dp = defaultCornerRadius,
    sliderStroke: Stroke? = null,
    sliderElevation: Dp? = null,
    onAlphaChanged: ((alpha: Float) -> Unit)? = null,
    onAlphaConfirmed: ((alpha: Float) -> Unit)? = null,
) {
    Slider(
        modifier = modifier,
        value = alpha,
        isVertical = isVertical,
        slider = {
            SliderPalette.NormalSlider(
                isVertical = isVertical,
                sliderWidth = sliderWidth,
                sliderBackground = Background.BrushShape(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0f, 0f, 0f, if (isVertical) 1f else 0f),
                            Color(0f, 0f, 0f, if (isVertical) 0f else 1f)
                        ), tileMode = TileMode.Repeated
                    ), RoundedCornerShape(sliderCornerRadius)
                ),
                sliderStroke = sliderStroke,
                sliderElevation = sliderElevation
            )
        },
        thumb = thumb,
        onValueChanged = onAlphaChanged,
        onValueConfirmed = onAlphaConfirmed
    )

}

@Composable
fun AlphaSlider(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = DEFAULT_ALPHA,
    isVertical: Boolean = false,
    thumbSize: DpSize = defaultThumbSize,
    thumbShape: Shape = CircleShape,
    thumbStroke: Stroke? = null,
    thumbBackgroundColor: Color? = null,
    thumbElevation: Dp? = null,
    sliderWidth: Dp = defaultSliderWidth,
    sliderCornerRadius: Dp = defaultCornerRadius,
    sliderStroke: Stroke? = null,
    sliderElevation: Dp? = null,
    onAlphaChanged: ((alpha: Float) -> Unit)? = null,
    onAlphaConfirmed: ((alpha: Float) -> Unit)? = null,
) {
    AlphaSlider(
        modifier = modifier,
        alpha = alpha,
        isVertical = isVertical,
        sliderWidth = sliderWidth,
        sliderCornerRadius = sliderCornerRadius,
        sliderStroke = sliderStroke,
        sliderElevation = sliderElevation,
        thumb = {
            ThumbPalette.Thumb(
                thumbBackgroundColor = thumbBackgroundColor ?: Color(0f, 0f, 0f, alpha),
                thumbSize = thumbSize,
                thumbStroke = thumbStroke,
                thumbShape = thumbShape,
                thumbElevation = thumbElevation,
            )
        },
        onAlphaChanged = onAlphaChanged,
        onAlphaConfirmed = onAlphaConfirmed
    )

}

@Preview
@Composable
fun PreviewAlphaSlider() {
    var alpha by remember {
        mutableStateOf(0f)
    }
    Column {
        AlphaSlider(
            alpha = alpha,
            isVertical = false,
            thumbBackgroundColor = Color.White,
            thumbElevation = 4.dp,
            onAlphaChanged = { alpha = it }
        )
        Spacer(modifier = Modifier.height(30.dp))
        AlphaSlider(
            Modifier.height(200.dp),
            alpha = alpha,
            isVertical = true,
            onAlphaChanged = { alpha = it },
            thumbShape = RectangleShape,
            thumbSize = DpSize(
                (defaultThumbRadius + defaultThumbStrokeWidth) * 2,
                (defaultThumbRadius + defaultThumbStrokeWidth) / 2
            ),
            sliderCornerRadius = 0.dp
        )
        Box(Modifier.fillMaxWidth()) {
            BasicText(
                modifier = Modifier.align(Alignment.Center), text = "Alpha : $alpha"
            )
        }
    }
}