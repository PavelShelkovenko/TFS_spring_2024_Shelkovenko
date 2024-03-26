package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
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
        color = resources.getColor(R.color.gray)
    }

    private var iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.white)
        style = Paint.Style.STROKE
        strokeWidth = 6f
        textSize = 16f.toSp(context)
    }

    private val reactionCount = "1"
    private val emojiStringHelper = "123"
    private var reactionCountBounds = Rect()
    private var emojiBounds = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        iconPaint.getTextBounds(reactionCount, 0, reactionCount.length, reactionCountBounds)
        iconPaint.getTextBounds(emojiStringHelper, 0, emojiStringHelper.length, emojiBounds)

        val textHeight = reactionCountBounds.height()

        val desiredWidth = (reactionCountBounds.width() + emojiBounds.width() + 50f + paddingRight + paddingLeft).toInt()
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
        const val DEFAULT_CORNER_RADIUS = 35f
        const val DEFAULT_PADDING = 5f
    }
}