/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import androidx.annotation.IntDef
import io.github.naverz.pinocchio.hsvpicker.view.Orientation.Companion.HORIZONTAL
import io.github.naverz.pinocchio.hsvpicker.view.Orientation.Companion.VERTICAL

@IntDef(HORIZONTAL, VERTICAL)
annotation class Orientation {
    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}