/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.unit.dp
import io.github.naverz.pinocchio.hsvpicker.compose.panel.SaturationValuePanel
import io.github.naverz.pinocchio.hsvpicker.compose.slider.AlphaSlider
import io.github.naverz.pinocchio.hsvpicker.compose.slider.HueSlider

class MainActivityCompose : ComponentActivity() {
    @OptIn(ExperimentalGraphicsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var hue by remember {
                mutableStateOf(0f)
            }
            var saturation by remember {
                mutableStateOf(1f)
            }
            var value by remember {
                mutableStateOf(1f)
            }
            var alpha by remember {
                mutableStateOf(1f)
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xffd3d3d3))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp)
                        .background(
                            Color.hsv(hue, saturation, value, alpha),
                            RoundedCornerShape(21.dp)
                        )
                )
                Row {
                    Column(Modifier.weight(1f)) {
                        HueSlider(
                            hue = hue,
                            isVertical = false,
                            onHueChanged = { hue = it }
                        )
                        SaturationValuePanel(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            saturation = saturation,
                            value = value,
                            hue = hue,
                            alpha = alpha,
                            onSaturationValueChanged = { newSaturation: Float, newValue: Float ->
                                saturation = newSaturation
                                value = newValue
                            }
                        )

                    }
                    AlphaSlider(
                        Modifier
                            .height(200.dp),
                        alpha = alpha,
                        isVertical = true,
                        thumbBackgroundColor = Color.White,
                        onAlphaChanged = { alpha = it }
                    )
                }
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp, bottom = 20.dp)
                ) {
                    BasicText(
                        modifier = Modifier
                            .background(Color.White),
                        text = "hue : $hue\nsaturation : $saturation\nvalue: $value\nalpha : $alpha"
                    )
                }
            }
        }
    }
}