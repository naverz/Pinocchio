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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.data.background
import io.github.naverz.pinocchio.slider.compose.data.stroke

object SliderPalette {

    @Composable
    fun BalancingSlider(
        @FloatRange(from = 0.0, to = 1.0)
        value: Float,
        isVertical: Boolean,
        sliderWidth: Dp,
        activeBrush: Brush,
        inactivateBrush: Brush,
        sliderStroke: Stroke? = null,
        sliderElevation: Dp? = null,
        sliderCornerShape: Shape? = null,
    ) {
        val safeValue = value.coerceIn(0f, 1f)
        val modifier = Modifier
            .run {
                if (isVertical) {
                    fillMaxHeight()
                        .width(sliderWidth)
                } else {
                    fillMaxWidth()
                        .height(sliderWidth)
                }
            }.run {
                if (sliderElevation != null) {
                    shadow(sliderElevation, sliderCornerShape ?: RectangleShape)
                } else {
                    this
                }
            }.run {
                if (sliderStroke != null) {
                    stroke(sliderStroke, sliderCornerShape ?: RectangleShape)
                } else {
                    this
                }
            }.run {
                if (sliderCornerShape != null) {
                    clip(sliderCornerShape)
                } else {
                    this
                }
            }
        if (safeValue == 0.5f) {
            Box(
                modifier
                    .background(inactivateBrush)
            )
        } else if (!isVertical) {
            Row(modifier) {
                if (safeValue < 0.5) {
                    if (safeValue > 0) {
                        Box(
                            Modifier
                                .weight(safeValue)
                                .fillMaxHeight()
                                .background(inactivateBrush)
                        )
                    }
                    Box(
                        Modifier
                            .weight(0.5f - safeValue)
                            .fillMaxHeight()
                            .background(activeBrush)
                    )
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .background(inactivateBrush)
                    )
                } else {
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .background(inactivateBrush)
                    )
                    Box(
                        Modifier
                            .weight(safeValue - 0.5f)
                            .fillMaxHeight()
                            .background(activeBrush)
                    )
                    if (safeValue < 1f) {
                        Box(
                            Modifier
                                .weight(1f - safeValue)
                                .fillMaxHeight()
                                .background(inactivateBrush)
                        )
                    }
                }
            }

        } else {
            Column(modifier) {
                if (safeValue < 0.5f) {
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .background(inactivateBrush)
                    )
                    Box(
                        Modifier
                            .weight(0.5f - safeValue)
                            .fillMaxWidth()
                            .background(activeBrush)
                    )
                    if (safeValue > 0f) {
                        Box(
                            Modifier
                                .weight(safeValue)
                                .fillMaxWidth()
                                .background(inactivateBrush)
                        )
                    }

                } else {
                    if (safeValue < 1f) {
                        Box(
                            Modifier
                                .weight(1f - safeValue)
                                .fillMaxWidth()
                                .background(inactivateBrush)
                        )
                    }
                    Box(
                        Modifier
                            .weight(safeValue - 0.5f)
                            .fillMaxWidth()
                            .background(activeBrush)
                    )
                    Box(
                        Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .background(inactivateBrush)
                    )
                }
            }
        }
    }

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
        val containerModifier = Modifier
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

        if (isVertical) {
            Column(modifier = containerModifier) {
                if (activeValue != 1f) {
                    Box(
                        Modifier
                            .weight(1 - activeValue.coerceAtLeast(0f))
                            .fillMaxWidth()
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
                if (activeValue != 0f) {
                    Box(
                        Modifier
                            .weight(activeValue.coerceAtLeast(0f))
                            .fillMaxWidth()
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
            }
        } else {
            Row(modifier = containerModifier) {
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
}

@Preview
@Composable
fun PreviewBalancingSliderPalette() {
    SliderPalette.BalancingSlider(
        value = 0.3f,
        isVertical = false,
        sliderWidth = 30.dp,
        activeBrush = SolidColor(Color.Red),
        inactivateBrush = SolidColor(Color.Gray),
        sliderCornerShape = RoundedCornerShape(21.dp),
        sliderStroke = Stroke.WidthColor(3.dp, Color.White)
    )
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