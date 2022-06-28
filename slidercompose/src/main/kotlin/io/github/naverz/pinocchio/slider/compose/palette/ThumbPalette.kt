/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose.palette

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.data.stroke
import io.github.naverz.pinocchio.slider.compose.defaultThumbRadius
import io.github.naverz.pinocchio.slider.compose.defaultThumbSize
import io.github.naverz.pinocchio.slider.compose.defaultThumbStroke
import io.github.naverz.pinocchio.slider.compose.defaultThumbStrokeWidth

object ThumbPalette {
    @Composable
    fun Thumb(
        thumbBackgroundColor: Color,
        thumbSize: DpSize = defaultThumbSize,
        thumbShape: Shape = CircleShape,
        thumbStroke: Stroke? = null,
        thumbElevation: Dp? = null,
    ) {
        Box(
            Modifier
                .size(thumbSize)
                .run {
                    if (thumbElevation != null) shadow(thumbElevation, thumbShape)
                    else this
                }
                .run {
                    if (thumbStroke != null) stroke(thumbStroke, thumbShape)
                    else this
                }
                .background(thumbBackgroundColor, thumbShape)
        )
    }

    @Composable
    fun CircleThumb(
        thumbRadius: Dp,
        color: Color,
        thumbElevation: Dp? = null,
        thumbStroke: Stroke? = null,
    ) {
        Box(Modifier
            .size(
                width = thumbRadius * 2,
                height = thumbRadius * 2
            )
            .run {
                if (thumbElevation != null) shadow(thumbElevation, CircleShape)
                else this
            }
            .run {
                if (thumbStroke != null) stroke(thumbStroke, CircleShape)
                else this
            }
            .background(color, CircleShape)

        )
    }

    @Composable
    fun CircleThumb(
        thumbRadius: Dp,
        brush: Brush,
        alpha: Float = 1f,
        thumbElevation: Dp? = null,
        thumbStroke: Stroke? = null,
    ) {
        Box(Modifier
            .run {
                if (thumbElevation != null) shadow(thumbElevation, CircleShape)
                else this
            }
            .run {
                if (thumbStroke != null) stroke(thumbStroke, CircleShape)
                else this
            }
            .background(brush, CircleShape, alpha)
            .size(
                width = thumbRadius * 2,
                height = thumbRadius * 2
            )
        )
    }


    @Composable
    fun RectThumb(
        thumbSize: DpSize,
        color: Color,
        roundCornerRadius: Dp = Dp(0f),
        thumbElevation: Dp? = null,
        thumbStroke: Stroke? = null,
    ) {
        Box(
            Modifier
                .run {
                    if (thumbElevation != null)
                        shadow(thumbElevation, RoundedCornerShape(roundCornerRadius))
                    else
                        this
                }
                .run {
                    if (thumbStroke != null) stroke(
                        thumbStroke,
                        RoundedCornerShape(roundCornerRadius)
                    )
                    else this
                }
                .background(color, RoundedCornerShape(roundCornerRadius))
                .size(thumbSize)
        )
    }

    @Composable
    fun RectThumb(
        thumbSize: DpSize,
        brush: Brush,
        alpha: Float = 1f,
        roundCornerRadius: Dp = Dp(0f),
        thumbElevation: Dp? = null,
        thumbStroke: Stroke? = null,
    ) {
        Box(
            Modifier
                .run {
                    if (thumbElevation != null)
                        shadow(thumbElevation, RoundedCornerShape(roundCornerRadius))
                    else
                        this
                }
                .run {
                    if (thumbStroke != null) stroke(
                        thumbStroke,
                        RoundedCornerShape(roundCornerRadius)
                    )
                    else this
                }
                .background(brush, RoundedCornerShape(roundCornerRadius), alpha)
                .size(thumbSize)
        )
    }

}

@Preview
@Composable
fun CircleThumbPreview() {
    ThumbPalette.CircleThumb(
        thumbRadius = 18.dp,
        color = Color.Black,
        thumbStroke = Stroke.WidthColor(3.dp, Color.White),
        thumbElevation = 4f.dp
    )
}

@Preview
@Composable
fun RectThumbPreview() {
    ThumbPalette.RectThumb(
        thumbSize = DpSize(
            30.dp,
            30.dp
        ),
        color = Color.Black,
        thumbStroke = Stroke.WidthColor(3.dp, Color.White),
        thumbElevation = 4f.dp
    )
}

