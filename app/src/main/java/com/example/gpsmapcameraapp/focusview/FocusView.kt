package com.example.gpsmapcameraapp.focusview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class FocusView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var focusShape: Int = FOCUS_SHAPE_CIRCLE // Default to circle shape
    private var focusColor: Int = Color.WHITE
    private var focusSize: Int = DEFAULT_FOCUS_SIZE
    private val paint: Paint = Paint()
    private var isManualFocus = false

    private var colorChangeRunnable: Runnable? = null

    companion object {
        const val FOCUS_SHAPE_SQUARE = 0
        const val FOCUS_SHAPE_CIRCLE = 1
        const val DEFAULT_FOCUS_SIZE = 150 // Default size of the focus icon
    }

    init {
        paint.color = focusColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
    }

    fun setFocusShape(shape: Int) {
        focusShape = shape
        invalidate()
    }

    fun setFocusColor(color: Int) {
        focusColor = color
        paint.color = focusColor
        invalidate()
    }

    fun setFocusSize(size: Int) {
        focusSize = size
        invalidate()
    }

    fun setManualFocus(isManual: Boolean) {
        isManualFocus = isManual
        if (isManualFocus && focusShape == FOCUS_SHAPE_SQUARE) {
            // If manual focus and shape is square, change color to green after 2 seconds
            colorChangeRunnable = Runnable {
                focusColor = Color.GREEN
                paint.color = focusColor
                invalidate()
            }
            postDelayed(colorChangeRunnable, 2000)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = focusSize / 2f

        if (focusShape == FOCUS_SHAPE_SQUARE && isManualFocus) {
            canvas.drawRect(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                paint
            )
        } else {
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        colorChangeRunnable?.let { removeCallbacks(it) }
    }
}