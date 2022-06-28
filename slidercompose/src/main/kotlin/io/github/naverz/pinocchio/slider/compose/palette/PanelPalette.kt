/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.slider.compose.palette

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.slider.compose.data.Background
import io.github.naverz.pinocchio.slider.compose.data.Stroke
import io.github.naverz.pinocchio.slider.compose.data.background
import io.github.naverz.pinocchio.slider.compose.data.stroke
import io.github.naverz.pinocchio.slider.compose.defaultCornerRadius
import io.github.naverz.pinocchio.slider.compose.defaultThumbStrokeWidth

object PanelPalette {
    @Composable
    fun Panel(
        panelBackground: Background,
        panelStroke: Stroke? = null,
        elevation: Dp? = null,
    ) {
        Box(Modifier
            .fillMaxSize()
            .run {
                if (elevation != null) shadow(
                    elevation = elevation,
                    shape = panelBackground.shape
                )
                else this
            }
            .run {
                if (panelStroke != null) stroke(
                    stroke = panelStroke,
                    shape = panelBackground.shape
                )
                else this
            }
            .background(panelBackground))
    }
}

@Composable
@Preview(heightDp = 300)
fun PreviewPanelPalette() {
    PanelPalette.Panel(
        panelBackground = Background.ColorShape(
            color = Color.Red,
            shape = RoundedCornerShape(defaultCornerRadius)
        ),
        panelStroke = Stroke.WidthColor(defaultThumbStrokeWidth, Color.White),
        elevation = 4f.dp
    )
}

