/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import io.github.naverz.hsvpicker.sample.R
import io.github.naverz.pinocchio.hsvpicker.view.ColorMediator

class MainActivityByKotlin : AppCompatActivity(), ColorMediator.OnColorChangedListener {

    private val colorMediator = ColorMediator()
    private var samplePanel: MaterialCardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        colorMediator.alphaSlider = findViewById(R.id.alpha_slider)
        colorMediator.satValPanel = findViewById(R.id.sat_val_panel)
        colorMediator.hueSlider = findViewById(R.id.hue_slider)
        colorMediator.setOnColorChangedListener(this)
        samplePanel = findViewById(R.id.sample_panel)
        samplePanel?.setCardBackgroundColor(colorMediator.currentColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        colorMediator.detach()
    }

    override fun onColorChanged(color: Int) {
        samplePanel?.setCardBackgroundColor(color)
    }

    override fun onColorConfirmed(color: Int) {
        Toast.makeText(this, "onColorConfirm : $color", Toast.LENGTH_LONG).show()
    }
}