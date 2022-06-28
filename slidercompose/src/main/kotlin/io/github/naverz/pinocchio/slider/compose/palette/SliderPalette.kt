/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose.palette

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.data.background
import io.github.naverz.pinocchio.slider.compose.data.stroke
import io.github.naverz.pinocchio.slider.compose.defaultCornerRadius
import io.github.naverz.pinocchio.slider.compose.defaultSliderWidth

object SliderPalette {
    @Composable
    fun NormalSlider(
        isVertical: Boolean,
        sliderWidth: Dp,
        sliderBackground: Background,
        sliderStroke: Stroke? = null,
        sliderElevation: Dp? = null,
    ) {
        Box(
            modifier = Modifier
                .run {
                    if (isVertical) {
                        fillMaxHeight()
                            .width(sliderWidth)
                    } else {
                        fillMaxWidth()
                            .height(sliderWidth)
                    }
                }
                .run {
                    if (sliderElevation != null) shadow(sliderElevation, sliderBackground.shape)
                    else this
                }
                .run {
                    if (sliderStroke != null) stroke(sliderStroke, sliderBackground.shape)
                    else this
                }
                .background(sliderBackground)
        )
    }


    @Composable
    fun ActiveSlider(
        @FloatRange(from = 0.0, to = 1.0)
        activeValue: Float,
        isVertical: Boolean,
        sliderWidth: Dp,
        activeBrush: Brush,
        inactivateBrush: Brush,
        sliderStroke: Stroke? = null,
        sliderElevation: Dp? = null,
        sliderCornerShape: RoundedCornerShape = RoundedCornerShape(0.dp),
    ) {
        Row(
            modifier = Modifier
                .run {
                    if (isVertical) {
                        fillMaxHeight()
                            .width(sliderWidth)
                    } else {
                        fillMaxWidth()
                            .height(sliderWidth)
                    }
                }.run {
                    if (sliderElevation != null) shadow(sliderElevation, sliderCornerShape)
                    else this
                }.run {
                    if (sliderStroke != null) {
                        stroke(sliderStroke, sliderCornerShape)
                    } else {
                        this
                    }
                }
        ) {
            if (activeValue != 0f) {
                Box(
                    Modifier
                        .weight(activeValue.coerceAtLeast(0f))
                        .fillMaxHeight()
                        .background(
                            activeBrush,
                            if (activeValue != 1f) {
                                sliderCornerShape.copy(
                                    topEnd = CornerSize(0.dp),
                                    bottomEnd = CornerSize(0.dp)
                                )
                            } else {
                                sliderCornerShape
                            }
                        )
                )
            }


            if (activeValue != 1f) {
                Box(
                    Modifier
                        .weight(1 - activeValue.coerceAtLeast(0f))
                        .fillMaxHeight()
                        .background(
                            inactivateBrush,
                            if (activeValue != 0f) {
                                sliderCornerShape.copy(
                                    topStart = CornerSize(0.dp),
                                    bottomStart = CornerSize(0.dp)
                                )
                            } else {
                                sliderCornerShape
                            }
                        )
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewActiveSliderPalette() {
    SliderPalette.ActiveSlider(
        activeValue = 0.5f,
        sliderWidth = 30.dp,
        isVertical = false,
        activeBrush = SolidColor(Color.Gray),
        inactivateBrush = SolidColor(Color.Red),
        sliderCornerShape = RoundedCornerShape(21.dp),
        sliderStroke = Stroke.WidthColor(3.dp, Color.White),
    )
}

@Preview
@Composable
fun PreviewNormalSliderPalette() {
    SliderPalette.NormalSlider(
        isVertical = false,
        sliderWidth = 30.dp,
        sliderBackground = Background.ColorShape(
            Color.Red,
            RoundedCornerShape(21.dp)
        ),
        sliderStroke = Stroke.WidthColor(3.dp, Color.White),
        sliderElevation = 4.dp,
    )
}