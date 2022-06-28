/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Stroke

@FloatRange(from = 0.0, to = 1.0)
const val DEFAULT_SLIDER_VALUE = 1f

@FloatRange(from = 0.0, to = 1.0)
const val DEFAULT_SLIDER_PANEL_X_VALUE = 1f

@FloatRange(from = 0.0, to = 1.0)
const val DEFAULT_SLIDER_PANEL_Y_VALUE = 1f

@FloatRange(from = 0.0, to = 1.0)
const val DEFAULT_ALPHA = DEFAULT_SLIDER_VALUE

@FloatRange(from = 0.0, to = 360.0)
const val DEFAULT_HSV_HUE = DEFAULT_SLIDER_VALUE * 360f

val defaultThumbRadius = 15.dp

val defaultCornerRadius = 21.dp

val defaultThumbStrokeWidth = 3.dp

val defaultSliderWidth = defaultThumbRadius * 2

val defaultThumbSize = DpSize(
    (defaultThumbRadius + defaultThumbStrokeWidth) * 2,
    (defaultThumbRadius + defaultThumbStrokeWidth) * 2
)
val defaultThumbStroke: Stroke = Stroke.WidthColor(defaultThumbStrokeWidth, Color.White)