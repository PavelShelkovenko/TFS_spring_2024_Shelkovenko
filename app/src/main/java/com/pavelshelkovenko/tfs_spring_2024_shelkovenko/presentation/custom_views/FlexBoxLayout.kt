package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.toDp
import kotlin.math.min
import kotlin.math.roundToInt

class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private var verticalContentPadding: Int = 0
    private var horizontalContentPadding: Int = 0

    init {
        context.withStyledAttributes(attrs, R.styleable.FlexBoxLayout) {
            verticalContentPadding = getDimension(
                R.styleable.FlexBoxLayout_verticalContentPadding,
                DEFAULT_CONTENT_PADDING.toDp(context)
            ).roundToInt()

            horizontalContentPadding = getDimension(
                R.styleable.FlexBoxLayout_horizontalContentPadding,
                DEFAULT_CONTENT_PADDING.toDp(context)
            ).roundToInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var availableWidth = widthSize - paddingStart - paddingEnd
        var desiredWidth = DEFAULT_WIDTH

        children.forEach { currentChild ->
            if (availableWidth >= currentChild.measuredWidth) {
                availableWidth -= (currentChild.measuredWidth)
                desiredWidth = widthSize - availableWidth
            } else {
                desiredWidth = widthSize - availableWidth
            }
            availableWidth -= horizontalContentPadding
        }

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> paddingEnd + paddingStart + widthSize
            MeasureSpec.AT_MOST -> paddingEnd + paddingStart + min(desiredWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> paddingEnd + paddingStart + DEFAULT_WIDTH
            else -> error("Unreachable")
        }

        availableWidth = width - paddingStart - paddingEnd
        var lines = 1

        children.forEach { currentChild ->
            if (availableWidth >= currentChild.measuredWidth) {
                availableWidth -= (currentChild.measuredWidth)
            } else {
                lines += 1
                availableWidth = width - paddingStart - paddingEnd - currentChild.measuredWidth
            }
            availableWidth -= horizontalContentPadding
        }

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> paddingTop + paddingBottom + heightSize
            MeasureSpec.AT_MOST -> {
                if (childCount > 0) {
                    paddingTop + paddingBottom +
                            (lines) * (children.first().measuredHeight) +
                            (lines) * (verticalContentPadding)
                } else {
                    DEFAULT_HEIGHT
                }
            }
            MeasureSpec.UNSPECIFIED -> {
                if (childCount > 0) {
                    paddingTop + paddingBottom +
                            (lines) * (children.first().measuredHeight) +
                            (lines) * (verticalContentPadding)
                } else {
                    DEFAULT_HEIGHT
                }

            }

            else -> error("Unreachable")
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val availableWidth = measuredWidth - paddingStart - paddingEnd
        var widthPointer = paddingStart
        var heightPointer = paddingTop

        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            if (widthPointer + currentChild.measuredWidth > availableWidth + paddingStart) {
                widthPointer = paddingStart
                heightPointer += currentChild.measuredHeight + verticalContentPadding
            }
            currentChild.layout(
                widthPointer,
                heightPointer,
                widthPointer + currentChild.measuredWidth,
                heightPointer + currentChild.measuredHeight
            )
            widthPointer += (currentChild.measuredWidth + horizontalContentPadding)
        }
    }

    companion object {
        const val DEFAULT_CONTENT_PADDING = 0f
        const val DEFAULT_WIDTH = 200
        const val DEFAULT_HEIGHT = 20
    }
}