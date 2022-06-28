/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import androidx.annotation.IntRange

class AlphaSlider @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Slider(context, attrs, defStyle) {


    private val alphaColors = intArrayOf(
        Color.argb(0, 0, 0, 0),
        Color.argb(255, 0, 0, 0)
    )

    private var listener: OnAlphaChangedListener? = null
    var alpha = DEFAULT_ALPHA
        private set

    val color: Int
        get() = Color.argb(alpha, 0, 0, 0)

    interface OnAlphaChangedListener {
        fun onAlphaChanged(@IntRange(from = 0, to = 255) alpha: Int)
        fun onAlphaConfirmed(@IntRange(from = 0, to = 255) alpha: Int)
    }

    fun setOnAlphaChangedListener(listener: OnAlphaChangedListener?) {
        this.listener = listener
    }

    @JvmOverloads
    fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int?, callback: Boolean = false) {
        alpha ?: return
        this.alpha = alpha
        if (callback) {
            listener?.onAlphaChanged(this.alpha)
        }
        setValue((alpha / 255).toFloat(), callback)
    }

    override fun onBeforeDrawThumb(thumbPaint: Paint) {
        super.onBeforeDrawThumb(thumbPaint)
        if (thumbColor == null)
            thumbPaint.color = Color.argb(alpha, 0, 0, 0)
    }

    override fun onValueChanged(value: Float) {
        super.onValueChanged(value)
        alpha = (value * 255).toInt()
        listener?.onAlphaChanged((value * 255).toInt())

    }

    override fun onValueConfirmed(value: Float) {
        super.onValueConfirmed(value)
        listener?.onAlphaConfirmed((value * 255).toInt())
    }

    override fun onPanelSizeChanged(panelPaint: Paint, panel: RectF) {
        super.onPanelSizeChanged(panelPaint, panel)
        panelPaint.shader = LinearGradient(
            if (isVertical) 0f else panel.left,
            if (isVertical) panel.bottom else 0f,
            if (isVertical) 0f else panel.right,
            if (isVertical) panel.top else 0f,
            alphaColors,
            null,
            TileMode.REPEAT
        )
    }
}