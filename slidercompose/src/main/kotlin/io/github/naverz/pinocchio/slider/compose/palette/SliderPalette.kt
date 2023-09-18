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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.data.background
import io.github.naverz.pinocchio.slider.compose.data.stroke
import io.github.naverz.pinocchio.slider.compose.palette.property.NarrowSliderProperty

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

    @Composable
    fun NarrowSlider(
        property: NarrowSliderProperty,
        isVertical: Boolean,
        sliderWidth: Dp,
        sliderHeight: Dp
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .width(
                    if (isVertical)
                        property.maxSize()
                    else
                        sliderWidth
                )
                .height(
                    if (isVertical)
                        sliderHeight
                    else
                        property.maxSize()
                )
        ) {
            val startRadius = property.startPeekWidth.toPx() / 2
            val endRadius = property.endPeekWidth.toPx() / 2

            val leftTop = if (isVertical) {
                Offset(
                    x = size.width / 2 - startRadius,
                    y = startRadius
                )
            } else {
                Offset(
                    x = startRadius,
                    y = size.height / 2 - startRadius
                )
            }

            val rightTop = if (isVertical) {
                Offset(
                    x = size.width / 2 + startRadius,
                    y = startRadius
                )
            } else {
                Offset(
                    x = size.width - endRadius,
                    y = size.height / 2 - endRadius
                )
            }

            val rightBottom = if (isVertical) {
                Offset(
                    x = size.width / 2 + endRadius,
                    y = size.height - endRadius
                )
            } else {
                Offset(
                    x = size.width - endRadius,
                    y = size.height / 2 + endRadius
                )
            }

            val leftBottom = if (isVertical) {
                Offset(
                    x = size.width / 2 - endRadius,
                    y = size.height - endRadius
                )
            } else {
                Offset(
                    x = startRadius,
                    y = size.height / 2 + startRadius
                )
            }

            val trianglePath = Path().apply {
                moveTo(x = leftTop.x, y = leftTop.y)
                lineTo(x = rightTop.x, y = rightTop.y)
                lineTo(x = rightBottom.x, y = rightBottom.y)
                lineTo(x = leftBottom.x, y = leftBottom.y)
                close()
            }

            drawIntoCanvas { canvas ->
                canvas.drawOutline(
                    outline = Outline.Generic(trianglePath),
                    paint = Paint().apply {
                        color = property.backgroundColor
                        pathEffect = PathEffect.cornerPathEffect((0.5).dp.toPx())
                    }
                )
                drawCircle(
                    color = property.backgroundColor,
                    center = if (isVertical) {
                        Offset(
                            x = size.width / 2,
                            y = startRadius
                        )
                    } else {
                        Offset(
                            x = startRadius,
                            y = size.height / 2
                        )
                    },
                    radius = startRadius
                )

                drawCircle(
                    color = property.backgroundColor,
                    center = if (isVertical) {
                        Offset(
                            x = size.width / 2,
                            y = size.height - endRadius
                        )
                    } else {
                        Offset(
                            x = size.width - endRadius,
                            y = size.height / 2
                        )
                    },
                    radius = endRadius
                )
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

@Preview
@Composable
fun PreviewNarrowSliderPalette() {
    Row (verticalAlignment = Alignment.CenterVertically){
        SliderPalette.NarrowSlider(
            property = NarrowSliderProperty(
                startPeekWidth = 30.dp,
                endPeekWidth = 15.dp
            ),
            isVertical = false,
            sliderWidth = 150.dp,
            sliderHeight = 30.dp
        )

        SliderPalette.NarrowSlider(
            property = NarrowSliderProperty(
                startPeekWidth = 15.dp,
                endPeekWidth = 30.dp
            ),
            isVertical = true,
            sliderWidth = 30.dp,
            sliderHeight = 150.dp
        )
    }
}