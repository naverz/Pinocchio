/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.compose.panel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.naverz.pinocchio.hsvpicker.compose.isRtl
import io.github.naverz.pinocchio.hsvpicker.compose.slider.AlphaSlider
import io.github.naverz.pinocchio.hsvpicker.compose.slider.HueSlider
import io.github.naverz.pinocchio.slider.compose.DEFAULT_ALPHA
import io.github.naverz.pinocchio.slider.compose.SlidePanel
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.defaultCornerRadius
import io.github.naverz.pinocchio.slider.compose.defaultThumbSize
import io.github.naverz.pinocchio.slider.compose.defaultThumbStroke
import io.github.naverz.pinocchio.slider.compose.palette.ThumbPalette

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
    val isRtl = isRtl()
    Box(
        Modifier
            .run {
                if (panelElevation != null)
                    shadow(panelElevation, RoundedCornerShape(cornerRadius))
                else
                    this
            }
            .run {
                if (panelStroke != null)
                    when (panelStroke) {
                        is Stroke.WidthBrush ->
                            this.border(
                                width = panelStroke.width,
                                brush = panelStroke.brush,
                                shape = RoundedCornerShape(cornerRadius)
                            )
                        is Stroke.WidthColor ->
                            this.border(
                                width = panelStroke.width,
                                color = panelStroke.color,
                                shape = RoundedCornerShape(cornerRadius)
                            )
                    }
                else
                    this
            }
            .clip(RoundedCornerShape(cornerRadius))) {
        AndroidView(
            factory = {
                CompatSaturationValuePanel(it, isRtl)
            },
            update = { panel ->
                panel.hue = hue
                panel.panelAlpha = alpha
                panel.isRtl = isRtl
            }
        )
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

@SuppressLint("ViewConstructor")
private class CompatSaturationValuePanel(context: Context, rtl: Boolean) :
    View(context) {

    var isRtl: Boolean = rtl
        set(value) {
            field = value
            changePanelColorShader()
            invalidate()
        }

    @FloatRange(from = 0.0, to = 360.0)
    var hue: Float = 0f
        set(value) {
            field = value
            changePanelColorShader()
            invalidate()
        }

    @FloatRange(from = 0.0, to = 1.0)
    var panelAlpha: Float = 1f
        set(value) {
            field = value
            changePanelColorShader()
            invalidate()
        }
    private val panel = RectF()

    private var panelPaint = Paint().apply {
        isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        panel.set(
            paddingLeft.toFloat(), paddingTop.toFloat(),
            (w - paddingRight).toFloat(), (h - paddingBottom).toFloat()
        )
        changePanelColorShader()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(panel, panelPaint)
    }

    private fun changePanelColorShader() {
        panelPaint.shader = ComposeShader(
            LinearGradient(
                if (isRtl) panel.right else panel.left,
                panel.top,
                if (isRtl) panel.right else panel.left,
                panel.bottom,
                -0x1,
                android.graphics.Color.argb(
                    (255 * panelAlpha).toInt(),
                    0, 0, 0
                ),
                Shader.TileMode.CLAMP
            ), LinearGradient(
                if (isRtl) panel.right else panel.left,
                panel.top,
                if (isRtl) panel.left else panel.right,
                panel.top,
                -0x1,
                android.graphics.Color.HSVToColor(
                    (255 * panelAlpha).toInt(),
                    floatArrayOf(hue, 1f, 1f)
                ),
                Shader.TileMode.CLAMP
            ), PorterDuff.Mode.MULTIPLY
        )
    }
}