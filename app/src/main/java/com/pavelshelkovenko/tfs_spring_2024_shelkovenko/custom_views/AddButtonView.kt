package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.toSp
import kotlin.math.min

class AddButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {


    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ResourcesCompat.getColor(resources,R.color.gray, null)
    }

    private var iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ResourcesCompat.getColor(resources, R.color.white, null)
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_ICON_STROKE_WIDTH
        textSize = DEFAULT_TEXT_SIZE.toSp(context)
    }

    private val stringHelper1 = "1"
    private val stringHelper2 = "123"
    private var stringHelper1Bounds = Rect()
    private var stringHelper2Bounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        iconPaint.getTextBounds(stringHelper1, 0, stringHelper1.length, stringHelper1Bounds)
        iconPaint.getTextBounds(stringHelper2, 0, stringHelper2.length, stringHelper2Bounds)

        val textHeight = stringHelper1Bounds.height()

        val desiredWidth = stringHelper1Bounds.width() + stringHelper2Bounds.width() + 50 + paddingRight + paddingLeft
        val desiredHeight = textHeight * 2 + paddingTop + paddingBottom + 25

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val widthCenter = measuredWidth / 2f
        val heightCenter = measuredHeight / 2f

        canvas.drawRoundRect(
            DEFAULT_PADDING + paddingLeft,
            DEFAULT_PADDING + paddingTop,
            measuredWidth - DEFAULT_PADDING - paddingRight,
            measuredHeight - DEFAULT_PADDING - paddingBottom,
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            backgroundPaint
        )
        canvas.drawLine(
            widthCenter,
            heightCenter + (heightCenter / 2f),
            widthCenter,
            heightCenter - (heightCenter / 2f),
            iconPaint
        )
        canvas.drawLine(
            widthCenter - (widthCenter / 3f),
            heightCenter ,
            widthCenter + (widthCenter / 3f),
            heightCenter,
            iconPaint
        )
    }

    companion object {
        const val DEFAULT_TEXT_SIZE = 16f
        const val DEFAULT_ICON_STROKE_WIDTH = 6f
        const val DEFAULT_CORNER_RADIUS = 35f
        const val DEFAULT_PADDING = 5f
    }
}