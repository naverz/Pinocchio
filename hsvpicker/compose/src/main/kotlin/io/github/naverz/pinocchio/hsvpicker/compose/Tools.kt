/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
internal fun Dp.toPx(): Float {
    return LocalDensity.current.run { this@toPx.toPx() }
}

@Composable
internal fun Int.toDp(): Dp {
    return LocalDensity.current.run { this@toDp.toDp() }
}

@Composable
internal fun Float.toDp(): Dp {
    return LocalDensity.current.run { this@toDp.toDp() }
}