/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

package io.github.naverz.pinocchio.hsvpicker.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import io.github.naverz.hsvpicker.view.R

open class SlidePanel @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val density = getContext().resources.displayMetrics.density
    private val fillMargin = 0.3f * density

    private val panelWithPadding = RectF()
    private val panel = RectF()

    private var listener: OnSliderPanelChangedListener? = null

    @FloatRange(from = 0.0, to = 1.0)
    var xValue = DEFAULT_SLIDER_PANEL_X_VALUE
        private set

    @FloatRange(from = 0.0, to = 1.0)
    var yValue = DEFAULT_SLIDER_PANEL_Y_VALUE
        private set

    interface OnSliderPanelChangedListener {
        fun onValueChanged(
            @FloatRange(from = 0.0, to = 1.0) x: Float, @FloatRange(from = 0.0, to = 1.0) y: Float
        )

        fun onValueConfirmed(
            @FloatRange(from = 0.0, to = 1.0) x: Float, @FloatRange(from = 0.0, to = 1.0) y: Float
        )
    }

    private val obtainStyledAttributes = attrs?.let { attributeSet ->
        context?.theme?.obtainStyledAttributes(attributeSet, R.styleable.SliderPanel, 0, 0)
    }

    var panelCornerRadius =
        obtainStyledAttributes?.getDimension(
            R.styleable.SliderPanel_panelCornerRadius,
            DEFAULT_CORNER_RADIUS_DP * density
        ) ?: (DEFAULT_CORNER_RADIUS_DP * density)
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var thumbColor: Int? =
        obtainStyledAttributes?.takeIf { it.hasValue(R.styleable.SliderPanel_thumbColor) }
            ?.getColor(R.styleable.SliderPanel_thumbColor, Color.TRANSPARENT)
        set(value) {
            field = value
            thumbPaint.color = value ?: Color.TRANSPARENT
            invalidate()
        }

    @ColorInt
    var thumbStrokeColor: Int? =
        obtainStyledAttributes?.takeIf { it.hasValue(R.styleable.SliderPanel_thumbStrokeColor) }
            ?.getColor(R.styleable.SliderPanel_thumbStrokeColor, Color.TRANSPARENT)
        set(value) {
            field = value
            this.thumbStrokePaint.color = value ?: Color.TRANSPARENT
            invalidate()
        }


    var thumbSize = obtainStyledAttributes
        ?.takeIf { it.hasValue(R.styleable.SliderPanel_thumbRadius) }
        ?.getDimension(R.styleable.SliderPanel_thumbRadius, DEFAULT_THUMB_RADIUS_DP * density)
        ?: (DEFAULT_THUMB_RADIUS_DP * density)
        set(value) {
            field = value
            invalidate()
        }

    var thumbStrokeWidth = obtainStyledAttributes
        ?.takeIf { it.hasValue(R.styleable.SliderPanel_thumbStrokeWidth) }
        ?.getDimension(
            R.styleable.SliderPanel_thumbStrokeWidth, DEFAULT_THUMB_STROKE_WIDTH_DP * density
        )
        ?: (DEFAULT_THUMB_STROKE_WIDTH_DP * density)
        set(value) {
            field = value
            invalidate()
        }


    private val panelPaint = Paint().apply {
        isAntiAlias = true
    }
    private val thumbPaint = Paint().apply {
        style = Paint.Style.FILL
        thumbColor?.let { thumbColor -> color = thumbColor }
        isAntiAlias = true
    }

    private val thumbStrokePaint = Paint().apply {
        isAntiAlias = true
        color = thumbStrokeColor ?: Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = thumbStrokeWidth
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        obtainStyledAttributes?.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        if (panel.width() <= 0 || panel.height() <= 0) return
        canvas.drawRoundRect(panel, panelCornerRadius, panelCornerRadius, panelPaint)
        val xThumbPosition = xValue * panel.width() + panel.left
        val yThumbPosition = (1f - yValue) * panel.height() + panel.top
        if (thumbStrokeWidth > 0) {
            canvas.drawCircle(
                xThumbPosition, yThumbPosition, thumbSize + thumbStrokeWidth / 2, thumbStrokePaint
            )
        }
        onBeforeDrawThumb(thumbPaint)
        if (thumbSize > 0) {
            canvas.drawCircle(xThumbPosition, yThumbPosition, thumbSize + fillMargin, thumbPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    private fun getSafeXYPercent(x: Float, y: Float): FloatArray {
        val rect = panel
        val width = panel.width()
        val height = panel.height()
        val xInBound = when {
            x < rect.left -> 0f
            x > rect.right -> width
            else -> x - rect.left
        }
        val yInBound = when {
            y < rect.top -> 0f
            y > rect.bottom -> height
            else -> y - rect.top
        }
        return FloatArray(2).also { result ->
            result[0] = xInBound / width
            result[1] = 1f - yInBound / height
        }
    }

    private var startTouchPoint = Point()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var update = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTouchPoint.set(event.x.toInt(), event.y.toInt())
                update = processForThumbMove(event)
            }
            MotionEvent.ACTION_MOVE -> update = processForThumbMove(event)
            MotionEvent.ACTION_UP -> {
                update = processForThumbMove(event)
                listener?.onValueConfirmed(xValue, yValue)
                onValueConfirmed(xValue, yValue)
                performClick()
            }
        }
        if (update) {
            listener?.onValueChanged(xValue, yValue)
            onValueChanged(xValue, yValue)
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun processForThumbMove(event: MotionEvent): Boolean {
        var update = false
        val startX = startTouchPoint.x
        val startY = startTouchPoint.y
        if (panelWithPadding.contains(startX.toFloat(), startY.toFloat())) {
            val result = getSafeXYPercent(event.x, event.y)
            xValue = result[0]
            yValue = result[1]
            update = true
        }
        return update
    }

    @JvmOverloads
    fun setXY(
        @FloatRange(from = 0.0, to = 1.0) x: Float? = null,
        @FloatRange(from = 0.0, to = 1.0) y: Float? = null,
        callback: Boolean = false
    ) {
        if (x == null && y == null) {
            return
        }
        x?.let { xValue = it }
        y?.let { yValue = it }
        if (callback) {
            listener?.onValueChanged(xValue, yValue)
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        panelWithPadding.set(
            paddingLeft.toFloat(), paddingTop.toFloat(),
            (w - paddingRight).toFloat(), (h - paddingBottom).toFloat()
        )
        val left = thumbSize + thumbStrokeWidth + paddingLeft
        val top = thumbSize + thumbStrokeWidth + paddingTop
        val right = w - thumbSize - thumbStrokeWidth - paddingRight
        val bottom = h - thumbSize - thumbStrokeWidth - paddingBottom
        val width = right - left
        val height = bottom - top
        panel.set(left, top, left + width, top + height)
        onPanelSizeChanged(panelPaint, panel)
    }

    open fun onValueChanged(
        @FloatRange(from = 0.0, to = 1.0) x: Float, @FloatRange(from = 0.0, to = 1.0) y: Float
    ) {
    }

    open fun onValueConfirmed(
        @FloatRange(from = 0.0, to = 1.0) x: Float, @FloatRange(from = 0.0, to = 1.0) y: Float
    ) {
    }

    open fun onBeforeDrawThumb(thumbPaint: Paint) {}
    open fun onPanelSizeChanged(panelPaint: Paint, panel: RectF) {}

    fun setOnSliderPanelChangedListener(listener: OnSliderPanelChangedListener?) {
        this.listener = listener
    }

}