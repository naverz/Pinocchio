package io.github.naverz.pinocchio.slider.compose.palette.property

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class NarrowSliderProperty (
    val startPeekWidth: Dp,
    val endPeekWidth: Dp,
    val backgroundColor: Color = Color.Gray
) {
    fun maxSize() = if (startPeekWidth >= endPeekWidth) startPeekWidth else endPeekWidth
}