/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

@FloatRange(from = 0.0, to = 1.0)
internal const val DEFAULT_SLIDER_VALUE = 1f

@FloatRange(from = 0.0, to = 1.0)
internal const val DEFAULT_SLIDER_PANEL_X_VALUE = 1f

@FloatRange(from = 0.0, to = 1.0)
internal const val DEFAULT_SLIDER_PANEL_Y_VALUE = 1f

@IntRange(from = 0, to = 255)
internal const val DEFAULT_ALPHA = (DEFAULT_SLIDER_VALUE * 255).toInt()

@FloatRange(from = 0.0, to = 360.0)
internal const val DEFAULT_HSV_HUE = DEFAULT_SLIDER_VALUE * 360f

@FloatRange(from = 0.0, to = 1.0)
internal const val DEFAULT_HSV_SATURATION = DEFAULT_SLIDER_PANEL_X_VALUE

@FloatRange(from = 0.0, to = 1.0)
internal const val DEFAULT_HSV_VALUE = DEFAULT_SLIDER_PANEL_Y_VALUE

internal const val DEFAULT_THUMB_RADIUS_DP = 15f

internal const val DEFAULT_CORNER_RADIUS_DP = 21f

internal const val DEFAULT_THUMB_STROKE_WIDTH_DP = 3f

internal const val DEFAULT_SLIDER_WIDTH_DP = DEFAULT_THUMB_RADIUS_DP * 2