/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import android.graphics.Color
import androidx.annotation.ColorInt

class ColorMediator : SatValPanel.OnSatValChangedListener, HueSlider.OnHueChangedListener,
    AlphaSlider.OnAlphaChangedListener {
    interface OnColorChangedListener {
        fun onColorChanged(@ColorInt color: Int)
        fun onColorConfirmed(@ColorInt color: Int)
    }

    private var onColorChangedListener: OnColorChangedListener? = null

    @ColorInt
    var currentColor =
        Color.HSVToColor(
            DEFAULT_ALPHA, floatArrayOf(DEFAULT_HSV_HUE, DEFAULT_HSV_SATURATION, DEFAULT_HSV_VALUE)
        )
        private set

    var satValPanel: SatValPanel? = null
        set(value) {
            field = value
            field?.color?.let { currentColor = it }
            field?.setOnSatValChangedListener(this)
        }
    var hueSlider: HueSlider? = null
        set(value) {
            field = value
            field?.color?.let { currentColor = it }
            field?.setOnHueChangedListener(this)
        }
    var alphaSlider: AlphaSlider? = null
        set(value) {
            field = value
            field?.color?.let { currentColor = it }
            field?.setOnAlphaChangedListener(this)
        }

    fun detach() {
        satValPanel?.setOnSatValChangedListener(null)
        hueSlider?.setOnHueChangedListener(null)
        alphaSlider?.setOnAlphaChangedListener(null)
    }

    override fun onSatValChanged(saturation: Float, value: Float) {
        val color = getColor(saturation, value)
        currentColor = color
        onColorChangedListener?.onColorChanged(color)
    }

    override fun onSatValConfirmed(saturation: Float, value: Float) {
        onColorChangedListener?.onColorConfirmed(getColor(saturation, value))
    }

    override fun onHueChanged(hue: Float) {
        satValPanel?.setHSV(hue = hue)
        val color = getColor(hue)
        currentColor = color
        onColorChangedListener?.onColorChanged(color)
    }

    override fun onHueConfirmed(hue: Float) {
        satValPanel?.setHSV(hue = hue)
        onColorChangedListener?.onColorConfirmed(getColor(hue))
    }

    override fun onAlphaChanged(alpha: Int) {
        satValPanel?.setHSV(alpha = alpha.toFloat() / 255f)
        val color = getColor(alpha)
        currentColor = color
        onColorChangedListener?.onColorChanged(color)
    }

    override fun onAlphaConfirmed(alpha: Int) {
        satValPanel?.setHSV(alpha = alpha.toFloat() / 255f)
        onColorChangedListener?.onColorConfirmed(getColor(alpha))
    }

    fun setOnColorChangedListener(onColorChangedListener: OnColorChangedListener?) {
        this.onColorChangedListener = onColorChangedListener
    }

    private fun getColor(alpha: Int): Int {
        return Color.HSVToColor(
            alpha,
            floatArrayOf(
                hueSlider?.hue ?: DEFAULT_HSV_HUE,
                satValPanel?.saturation ?: DEFAULT_HSV_SATURATION,
                satValPanel?.value ?: DEFAULT_HSV_VALUE
            )
        )
    }

    private fun getColor(hue: Float): Int {
        return Color.HSVToColor(
            alphaSlider?.alpha ?: 255,
            floatArrayOf(
                hue,
                satValPanel?.saturation ?: DEFAULT_HSV_SATURATION,
                satValPanel?.value ?: DEFAULT_HSV_VALUE
            )
        )
    }

    private fun getColor(saturation: Float, value: Float): Int {
        return Color.HSVToColor(
            alphaSlider?.alpha ?: 255,
            floatArrayOf(
                hueSlider?.hue ?: DEFAULT_HSV_HUE, saturation, value
            )
        )
    }
}