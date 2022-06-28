/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import android.content.Context
import android.graphics.*
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import androidx.annotation.FloatRange

class SatValPanel @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SlidePanel(context, attrs, defStyle) {

    private var listener: OnSatValChangedListener? = null
    var alpha = DEFAULT_ALPHA
        private set
    var hue = DEFAULT_HSV_HUE
        private set
    var saturation = DEFAULT_HSV_SATURATION
        get() = xValue
        private set
    var value = DEFAULT_HSV_VALUE
        get() = yValue
        private set

    var color: Int
        get() = Color.HSVToColor(alpha, floatArrayOf(hue, saturation, value))
        set(color) {
            setColor(color, false)
        }

    private val hsvArray: FloatArray
        get() = floatArrayOf(hue, saturation, value)

    interface OnSatValChangedListener {
        fun onSatValChanged(
            @FloatRange(from = 0.0, to = 1.0) saturation: Float,
            @FloatRange(from = 0.0, to = 1.0) value: Float
        )

        fun onSatValConfirmed(
            @FloatRange(from = 0.0, to = 1.0) saturation: Float,
            @FloatRange(from = 0.0, to = 1.0) value: Float
        )
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onValueChanged(x: Float, y: Float) {
        super.onValueChanged(x, y)
        saturation = x
        value = y
        listener?.onSatValChanged(x, y)
    }

    override fun onValueConfirmed(x: Float, y: Float) {
        super.onValueConfirmed(x, y)
        listener?.onSatValConfirmed(x, y)
    }

    override fun onBeforeDrawThumb(thumbPaint: Paint) {
        super.onBeforeDrawThumb(thumbPaint)
        if (thumbColor == null)
            thumbPaint.color = Color.HSVToColor(alpha, floatArrayOf(hue, saturation, value))
    }

    private var lastLoadedPanel: RectF? = null
    private var lastLoadedPanelPaint: Paint? = null
    override fun onPanelSizeChanged(panelPaint: Paint, panel: RectF) {
        super.onPanelSizeChanged(panelPaint, panel)
        lastLoadedPanel = panel
        lastLoadedPanelPaint = panelPaint
        changePanelColorShader()
    }

    fun setOnSatValChangedListener(listener: OnSatValChangedListener?) {
        this.listener = listener
    }

    @JvmOverloads
    fun setHSV(
        @FloatRange(from = 0.0, to = 360.0) hue: Float? = null,
        @FloatRange(from = 0.0, to = 1.0) saturation: Float? = null,
        @FloatRange(from = 0.0, to = 1.0) value: Float? = null,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float? = null,
        callback: Boolean = false
    ) {
        if (hue == null && saturation == null && value == null && alpha == null) return
        hue?.let { this.hue = it }
        saturation?.let { this.saturation = it }
        value?.let { this.value = it }
        alpha?.let { this.alpha = (it * 255).toInt() }
        changePanelColorShader()
        if (callback) {
            listener?.onSatValChanged(this.saturation, this.value)
        }
        setXY(saturation ?: this.saturation, value ?: this.value, callback)
    }

    @JvmOverloads
    fun setColor(color: Int?, callback: Boolean = false) {
        color ?: return
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        setHSV(hsv[0], hsv[1], hsv[2], Color.alpha(color).toFloat() / 255f, callback)
    }

    private fun changePanelColorShader() {
        val paint = lastLoadedPanelPaint ?: return
        val panel = lastLoadedPanel ?: return
        paint.shader = ComposeShader(
            LinearGradient(
                panel.left, panel.top, panel.left, panel.bottom,
                -0x1, Color.argb(alpha, 0, 0, 0), TileMode.CLAMP
            ), LinearGradient(
                panel.left, panel.top, panel.right, panel.top,
                -0x1, Color.HSVToColor(alpha, floatArrayOf(hue, 1f, 1f)), TileMode.CLAMP
            ), PorterDuff.Mode.MULTIPLY
        )
    }
}