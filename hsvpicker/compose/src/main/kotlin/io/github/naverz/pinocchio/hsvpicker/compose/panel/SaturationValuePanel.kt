/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.compose.panel

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.hsvpicker.compose.slider.AlphaSlider
import io.github.naverz.pinocchio.hsvpicker.compose.slider.HueSlider
import io.github.naverz.pinocchio.hsvpicker.compose.toPx
import io.github.naverz.pinocchio.slider.compose.*
import io.github.naverz.pinocchio.slider.compose.palette.ThumbPalette
import io.github.naverz.pinocchio.slider.compose.data.Stroke

@Composable
fun SaturationValuePanel(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 360.0)
    hue: Float,
    @FloatRange(from = 0.0, to = 1.0)
    saturation: Float,
    @FloatRange(from = 0.0, to = 1.0)
    value: Float,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = DEFAULT_ALPHA,
    thumb: @Composable () -> Unit,
    panelCornerRadius: Dp = defaultCornerRadius,
    panelStroke: Stroke? = null,
    panelElevation: Dp? = null,
    onSaturationValueChanged: ((saturation: Float, value: Float) -> Unit)? = null,
    onSaturationValueConfirmed: ((saturation: Float, value: Float) -> Unit)? = null,
) {
    SlidePanel(
        modifier = modifier,
        x = saturation,
        y = value,
        onValueChanged = onSaturationValueChanged,
        onValueConfirmed = onSaturationValueConfirmed,
        thumb = thumb,
        panel = {
            BaseSaturationValuePanel(
                hue = hue,
                alpha = alpha,
                cornerRadius = panelCornerRadius,
                panelStroke = panelStroke,
                panelElevation = panelElevation
            )
        }
    )
}


@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun SaturationValuePanel(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 360.0)
    hue: Float,
    @FloatRange(from = 0.0, to = 1.0)
    saturation: Float,
    @FloatRange(from = 0.0, to = 1.0)
    value: Float,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = DEFAULT_ALPHA,
    thumbSize: DpSize = defaultThumbSize,
    thumbStroke: Stroke = defaultThumbStroke,
    thumbShape: Shape = CircleShape,
    thumbBackgroundColor: Color? = null,
    thumbElevation: Dp? = null,
    panelCornerRadius: Dp = defaultCornerRadius,
    panelStroke: Stroke? = null,
    panelElevation: Dp? = null,
    onSaturationValueChanged: ((saturation: Float, value: Float) -> Unit)? = null,
    onSaturationValueConfirmed: ((saturation: Float, value: Float) -> Unit)? = null,
) {
    SaturationValuePanel(
        modifier = modifier,
        hue = hue,
        saturation = saturation,
        value = value,
        alpha = alpha,
        panelCornerRadius = panelCornerRadius,
        panelStroke = panelStroke,
        panelElevation = panelElevation,
        thumb = {
            ThumbPalette.Thumb(
                thumbBackgroundColor = thumbBackgroundColor
                    ?: Color.hsv(hue = hue, saturation = saturation, value = value, alpha = alpha),
                thumbSize = thumbSize,
                thumbStroke = thumbStroke,
                thumbShape = thumbShape,
                thumbElevation = thumbElevation,
            )
        },
        onSaturationValueChanged = onSaturationValueChanged,
        onSaturationValueConfirmed = onSaturationValueConfirmed
    )
}



@OptIn(ExperimentalGraphicsApi::class)
@Composable
private fun BaseSaturationValuePanel(
    @FloatRange(from = 0.0, to = 360.0)
    hue: Float,
    @FloatRange(from = 0.0, to = 1.0)
    alpha: Float = DEFAULT_ALPHA,
    cornerRadius: Dp = 0.dp,
    panelStroke: Stroke? = null,
    panelElevation: Dp? = null
) {
    val baseGradientBrush = remember(alpha) {
        Brush.verticalGradient(
            listOf(
                Color(red = 1f, green = 1f, blue = 1f, alpha = alpha),
                Color(red = 0f, green = 0f, blue = 0f, alpha = alpha)
            )
        )
    }
    val currentHueGradient = remember(hue, alpha) {
        Brush.horizontalGradient(
            listOf(
                Color(red = 1f, green = 1f, blue = 1f, alpha = alpha),
                Color.hsv(hue = hue, saturation = 1f, value = 1f, alpha = alpha)
            )
        )
    }
    val cornerRadiusModel = CornerRadius(cornerRadius.toPx())
    Canvas(Modifier.fillMaxSize()
        .run {
            if (panelElevation != null) shadow(panelElevation, RoundedCornerShape(cornerRadius))
            else this
        }) {
        drawRoundRect(
            brush = baseGradientBrush,
            cornerRadius = cornerRadiusModel
        )
        drawRoundRect(
            brush = currentHueGradient,
            cornerRadius = cornerRadiusModel,
            blendMode = BlendMode.Multiply
        )
        when (panelStroke) {
            is Stroke.WidthBrush ->
                drawRoundRect(
                    panelStroke.brush,
                    cornerRadius = cornerRadiusModel,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(panelStroke.width.toPx())
                )
            is Stroke.WidthColor ->
                drawRoundRect(
                    panelStroke.color,
                    cornerRadius = cornerRadiusModel,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(panelStroke.width.toPx())
                )
            null -> Unit
        }
    }
}
@OptIn(ExperimentalGraphicsApi::class)
@Composable
@Preview
fun PreviewSaturationValuePanel() {
    var hue by remember {
        mutableStateOf(0f)
    }
    var saturation by remember {
        mutableStateOf(1f)
    }
    var value by remember {
        mutableStateOf(1f)
    }
    var alpha by remember {
        mutableStateOf(1f)
    }

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp)
                .background(
                    Color.hsv(hue, saturation, value, alpha),
                    RoundedCornerShape(defaultCornerRadius)
                )
        )
        Row {
            Column(Modifier.weight(1f)) {
                HueSlider(
                    hue = hue,
                    isVertical = false,
                    onHueChanged = { hue = it }
                )
                SaturationValuePanel(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    saturation = saturation,
                    value = value,
                    hue = hue,
                    alpha = alpha,
                    onSaturationValueChanged = { newSaturation: Float, newValue: Float ->
                        saturation = newSaturation
                        value = newValue
                    }
                )

            }
            AlphaSlider(
                Modifier
                    .height(200.dp),
                alpha = alpha,
                isVertical = true,
                thumbBackgroundColor = Color.White,
                onAlphaChanged = { alpha = it }
            )
        }
        Box(
            Modifier
                .align(CenterHorizontally)
                .padding(top = 10.dp, bottom = 20.dp)
        ) {
            BasicText(
                modifier = Modifier
                    .background(Color.White),
                text = "hue : $hue\nsaturation : $saturation\nvalue: $value\nalpha : $alpha"
            )
        }
    }
}