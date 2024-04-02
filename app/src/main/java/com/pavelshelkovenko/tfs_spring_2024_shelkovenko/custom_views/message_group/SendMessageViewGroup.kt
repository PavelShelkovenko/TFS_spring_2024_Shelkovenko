package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.message_group

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.FlexBoxLayout
import kotlin.math.max


class SendMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : MessageViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        setWillNotDraw(false)
        backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.green, null)
        inflate(context, R.layout.send_message_view_group,this)
    }

    override fun onMeasure(parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        super.onMeasure(parentWidthMeasureSpec, parentHeightMeasureSpec)

        val widthMode = MeasureSpec.getMode(parentWidthMeasureSpec)
        val widthSize = MeasureSpec.getSize(parentWidthMeasureSpec)
        val heightMode = MeasureSpec.getMode(parentHeightMeasureSpec)
        val heightSize = MeasureSpec.getSize(parentHeightMeasureSpec)

        val textMessageView = getChildAt(0) as TextView
        val flexBoxLayout = getChildAt(1) as FlexBoxLayout

        textMessageView.text = textMessage

        measureChildren(parentWidthMeasureSpec,parentHeightMeasureSpec)

        val childrenHeight = textMessageView.measuredHeight + flexBoxLayout.measuredHeight + 2 * innerPadding
        val childrenWidth = max(textMessageView.measuredWidth, flexBoxLayout.measuredWidth)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY ->  widthSize
            MeasureSpec.AT_MOST -> childrenWidth
            MeasureSpec.UNSPECIFIED -> childrenWidth
            else -> error("Unreachable")
        }

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> paddingTop + paddingBottom + heightSize
            MeasureSpec.AT_MOST -> paddingTop + paddingBottom + childrenHeight
            MeasureSpec.UNSPECIFIED -> paddingTop + paddingBottom + childrenHeight
            else -> error("Unreachable")
        } + 2 * innerPadding

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val textMessageView = getChildAt(0)
        val flexBoxLayout = getChildAt(1)

        val widthPointer = measuredWidth - paddingEnd - textMessageView.measuredWidth
        var heightPointer = paddingTop

        textMessageView.layout(
            widthPointer,
            heightPointer,
            widthPointer + textMessageView.measuredWidth,
            heightPointer + textMessageView.measuredHeight + innerPadding
        )

        heightPointer += textMessageView.measuredHeight + 2 * innerPadding

        val right = measuredWidth - paddingEnd
        val left = right - flexBoxLayout.measuredWidth

        flexBoxLayout.layout(
            left,
            heightPointer ,
            right,
            heightPointer + flexBoxLayout.measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val textMessageView = getChildAt(0)

        val left = measuredWidth - paddingEnd - textMessageView.measuredWidth
        val top = paddingTop
        val right = left + textMessageView.measuredWidth
        val bottom = top + textMessageView.measuredHeight

        canvas.drawRoundRect(
            left.toFloat() - innerPadding,
            top.toFloat() - innerPadding,
            right.toFloat() + innerPadding,
            bottom.toFloat() + innerPadding,
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            backgroundPaint
        )
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString(DEFAULT_MESSAGE_TEXT_KEY, textMessage)
        bundle.putParcelable(INSTANCESTATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        setTextMessage(bundle.getString(ReceivedMessageViewGroup.DEFAULT_MESSAGE_TEXT_KEY).toString())
        super.onRestoreInstanceState(bundle.getParcelable(INSTANCESTATE_KEY))
        invalidate()
    }

    override fun getFlexBoxLayoutChild(): FlexBoxLayout {
        return getChildAt(1) as FlexBoxLayout
    }

    override fun getTextMessageChild(): TextView {
        return getChildAt(0) as TextView
    }

    companion object {
        const val DEFAULT_MESSAGE_TEXT_KEY = "key_for_message_text"
        const val INSTANCESTATE_KEY = "instanceState"
    }

}