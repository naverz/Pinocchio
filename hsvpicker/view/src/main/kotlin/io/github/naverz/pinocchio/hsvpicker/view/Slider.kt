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
import io.github.naverz.hsvpicker.view.*

open class Slider @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val density = getContext().resources.displayMetrics.density
    private val fillMargin = 0.3f * density

    private val panelWithPadding = RectF()
    private val panel = RectF()

    @FloatRange(from = 0.0, to = 1.0)
    var value = DEFAULT_SLIDER_VALUE
        private set
    private var listener: OnSliderChangedListener? = null

    interface OnSliderChangedListener {
        fun onValueChanged(@FloatRange(from = 0.0, to = 1.0) value: Float)
        fun onValueConfirmed(@FloatRange(from = 0.0, to = 1.0) value: Float)
    }

    private val obtainStyledAttributes = attrs?.let { attributeSet ->
        context?.theme?.obtainStyledAttributes(attributeSet, R.styleable.Slider, 0, 0)
    }

    var sliderCornerRadius =
        obtainStyledAttributes?.getDimension(
            R.styleable.Slider_sliderCornerRadius,
            DEFAULT_CORNER_RADIUS_DP * density
        ) ?: (DEFAULT_CORNER_RADIUS_DP * density)
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var thumbColor: Int? =
        obtainStyledAttributes?.takeIf { it.hasValue(R.styleable.Slider_thumbColor) }
            ?.getColor(R.styleable.Slider_thumbColor, Color.TRANSPARENT)
        set(value) {
            field = value
            thumbPaint.color = value ?: Color.TRANSPARENT
            invalidate()
        }

    @ColorInt
    var thumbStrokeColor: Int? =
        obtainStyledAttributes?.takeIf { it.hasValue(R.styleable.Slider_thumbStrokeColor) }
            ?.getColor(R.styleable.Slider_thumbStrokeColor, Color.TRANSPARENT)
        set(value) {
            field = value
            thumbStrokePaint.color = value ?: Color.TRANSPARENT
            invalidate()
        }


    var thumbRadius =
        obtainStyledAttributes?.getDimension(
            R.styleable.Slider_thumbRadius, DEFAULT_THUMB_RADIUS_DP * density
        ) ?: (DEFAULT_THUMB_RADIUS_DP * density)
        set(value) {
            field = value
            invalidate()
        }

    var thumbStrokeWidth =
        obtainStyledAttributes?.getDimension(
            R.styleable.Slider_thumbStrokeWidth, DEFAULT_THUMB_STROKE_WIDTH_DP * density
        ) ?: (DEFAULT_THUMB_STROKE_WIDTH_DP * density)
        set(value) {
            field = value
            invalidate()
        }

    var sliderWidth =
        obtainStyledAttributes?.getDimension(
            R.styleable.Slider_sliderWidth, DEFAULT_SLIDER_WIDTH_DP * density
        ) ?: (DEFAULT_SLIDER_WIDTH_DP * density)
        set(value) {
            field = value
            requestLayout()
        }

    @Orientation
    var sliderOrientation =
        obtainStyledAttributes?.getInt(R.styleable.Slider_sliderOrientation, Orientation.HORIZONTAL)
            ?: Orientation.HORIZONTAL
        set(value) {
            isVertical = value == Orientation.VERTICAL
            field = value
            requestLayout()
        }

    var isVertical = sliderOrientation == Orientation.VERTICAL
        private set

    private var halfPositionOfThumb = 0f

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
        canvas.drawRoundRect(panel, sliderCornerRadius, sliderCornerRadius, panelPaint)
        val xThumbPosition =
            if (isVertical) halfPositionOfThumb
            else value * panel.width() + panel.left
        val yThumbPosition =
            if (isVertical) (1 - value) * panel.height() + panel.top
            else halfPositionOfThumb
        if (thumbStrokeWidth > 0) {
            canvas.drawCircle(
                xThumbPosition, yThumbPosition, thumbRadius + thumbStrokeWidth / 2, thumbStrokePaint
            )
        }
        onBeforeDrawThumb(thumbPaint)
        if (thumbRadius > 0) {
            canvas.drawCircle(xThumbPosition, yThumbPosition, thumbRadius + fillMargin, thumbPaint)
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
                listener?.onValueConfirmed(value)
                onValueConfirmed(value)
                performClick()
            }
        }
        if (update) {
            listener?.onValueChanged(value)
            onValueChanged(value)
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun processForThumbMove(event: MotionEvent): Boolean {
        var update = false
        if (panelWithPadding.contains(startTouchPoint.x.toFloat(), startTouchPoint.y.toFloat())) {
            val point = if (isVertical) event.y else event.x
            val pointInbound = when {
                point < if (isVertical) panel.top else panel.left ->
                    0f
                point > if (isVertical) panel.bottom else panel.right ->
                    if (isVertical) panel.height() else panel.width()
                else ->
                    point - if (isVertical) panel.top else panel.left
            }
            value =
                if (isVertical) 1 - pointInbound / panel.height() else pointInbound / panel.width()
            update = true
        }
        return update
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val greaterValue = sliderWidth.coerceAtLeast((thumbRadius + thumbStrokeWidth) * 2)
            .plus(paddingBottom)
            .plus(paddingTop).toInt()

        if (isVertical) {
            setMeasuredDimension(greaterValue, MeasureSpec.getSize(heightMeasureSpec))
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), greaterValue)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        panelWithPadding.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (w - paddingRight).toFloat(),
            (h - paddingBottom).toFloat()
        )
        val thumbIsGreaterThanSliderWidth = thumbRadius + thumbStrokeWidth > sliderWidth / 2
        val diffThumbAndSlider = thumbRadius + thumbStrokeWidth - sliderWidth / 2
        val left =
            if (isVertical) paddingLeft + (if (thumbIsGreaterThanSliderWidth) diffThumbAndSlider else 0f)
            else thumbRadius + thumbStrokeWidth + paddingLeft
        val top =
            if (isVertical) paddingTop + thumbRadius + thumbStrokeWidth
            else paddingTop + (if (thumbIsGreaterThanSliderWidth) diffThumbAndSlider else 0f)

        val right =
            if (isVertical) w - paddingRight - (if (thumbIsGreaterThanSliderWidth) diffThumbAndSlider else 0f)
            else w - thumbRadius - paddingRight - thumbStrokeWidth
        val bottom =
            if (isVertical) h - paddingBottom - thumbRadius - thumbStrokeWidth
            else h - paddingBottom - (if (thumbIsGreaterThanSliderWidth) diffThumbAndSlider else 0f)
        val width = right - left
        val height = bottom - top

        panel.set(left, top, left + width, top + height)

        halfPositionOfThumb =
            if (isVertical) panel.left + panel.width() / 2
            else panel.top + panel.height() / 2
        onPanelSizeChanged(panelPaint, panel)
    }

    open fun onValueChanged(@FloatRange(from = 0.0, to = 1.0) value: Float) {}
    open fun onValueConfirmed(@FloatRange(from = 0.0, to = 1.0) value: Float) {}
    open fun onPanelSizeChanged(panelPaint: Paint, panel: RectF) {}
    open fun onBeforeDrawThumb(thumbPaint: Paint) {}

    open fun setOnSliderChangedListener(listener: OnSliderChangedListener) {
        this.listener = listener
    }

    @JvmOverloads
    fun setValue(@FloatRange(from = 0.0, to = 1.0) value: Float?, callback: Boolean = false) {
        value ?: return
        this.value = value
        if (callback) {
            listener?.onValueChanged(this.value)
        }
        invalidate()
    }
}