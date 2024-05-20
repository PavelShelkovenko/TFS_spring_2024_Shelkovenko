package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.message_group

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.FlexBoxLayout

class ReceivedMessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : MessageViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    var userAvatar: ImageView
        private set

    init {
        setWillNotDraw(false)
        backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.gray, null)
        inflate(context, R.layout.received_message_view_group, this)
        userAvatar = findViewById(R.id.received_user_avatar)
    }

    var userName = DEFAULT_USER_NAME
        private set




    override fun onMeasure(parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        super.onMeasure(parentWidthMeasureSpec, parentHeightMeasureSpec)

        val widthMode = MeasureSpec.getMode(parentWidthMeasureSpec)
        val widthSize = MeasureSpec.getSize(parentWidthMeasureSpec)
        val heightMode = MeasureSpec.getMode(parentHeightMeasureSpec)
        val heightSize = MeasureSpec.getSize(parentHeightMeasureSpec)

        val userAvatarView = getChildAt(0) as ImageView
        val userNameView = getChildAt(1) as TextView
        val textMessageView = getChildAt(2) as TextView
        val flexBoxLayout = getFlexBoxLayoutChild()

        userNameView.text = userName
        textMessageView.text = textMessage
        measureChild(userAvatarView, parentWidthMeasureSpec, parentHeightMeasureSpec)


        val availableForRestChildrenWidth =
            widthSize - userAvatarView.measuredWidth - paddingStart - paddingEnd - innerPadding
        val userNameWidthSpec =
            MeasureSpec.makeMeasureSpec(availableForRestChildrenWidth, MeasureSpec.AT_MOST)
        val textMessageWidthSpec =
            MeasureSpec.makeMeasureSpec(availableForRestChildrenWidth, MeasureSpec.AT_MOST)
        val flexBoxLayoutWidthSpec =
            MeasureSpec.makeMeasureSpec(availableForRestChildrenWidth, MeasureSpec.AT_MOST)

        measureChild(userNameView, userNameWidthSpec, parentHeightMeasureSpec)
        measureChild(textMessageView, textMessageWidthSpec, parentHeightMeasureSpec)
        measureChild(flexBoxLayout, flexBoxLayoutWidthSpec, parentHeightMeasureSpec)

        val childrenWidth = userAvatarView.measuredWidth + maxOf(
            textMessageView.measuredWidth,
            userNameView.measuredWidth,
            flexBoxLayout.measuredWidth
        ) + 2 * innerPadding
        val childrenHeight =
            userNameView.measuredHeight + textMessageView.measuredHeight + flexBoxLayout.measuredHeight

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> paddingStart + paddingEnd + widthSize
            MeasureSpec.AT_MOST -> paddingStart + paddingEnd + childrenWidth
            MeasureSpec.UNSPECIFIED -> paddingStart + paddingEnd + childrenWidth
            else -> error("Unreachable")
        } - paddingEnd

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> paddingTop + paddingBottom + heightSize
            MeasureSpec.AT_MOST -> paddingTop + paddingBottom + childrenHeight + 2 * innerPadding
            MeasureSpec.UNSPECIFIED -> paddingTop + paddingBottom + childrenHeight + 3 * innerPadding
            else -> error("Unreachable")
        }
        setMeasuredDimension(width, height)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val availableWidth = measuredWidth - paddingEnd
        var widthPointer = paddingStart
        var heightPointer = paddingTop

        val userAvatarView = getChildAt(0)
        val userNameView = getChildAt(1)
        val textMessageView = getChildAt(2)
        val flexBoxLayout = getFlexBoxLayoutChild()

        userAvatarView.layout(
            widthPointer,
            heightPointer,
            widthPointer + userAvatarView.measuredWidth,
            heightPointer + userAvatarView.measuredHeight
        )

        widthPointer += userAvatarView.measuredWidth + 2 * innerPadding

        var userNameWidth = userNameView.measuredWidth

        userNameWidth = if (userNameWidth + widthPointer < availableWidth) {
            widthPointer + userNameWidth
        } else {
            availableWidth
        }

        userNameView.layout(
            widthPointer,
            heightPointer,
            userNameWidth,
            heightPointer + userNameView.measuredHeight
        )

        heightPointer += userNameView.measuredHeight + innerPadding / 2

        var textMessageWidth = textMessageView.measuredWidth

        textMessageWidth = if (textMessageWidth + widthPointer < availableWidth) {
            widthPointer + textMessageWidth
        } else {
            availableWidth
        }

        textMessageView.layout(
            widthPointer,
            heightPointer,
            textMessageWidth,
            heightPointer + textMessageView.measuredHeight
        )

        heightPointer += textMessageView.measuredHeight + innerPadding

        var flexBoxLayoutWidth = flexBoxLayout.measuredWidth

        flexBoxLayoutWidth = if (flexBoxLayoutWidth + widthPointer < availableWidth) {
            widthPointer + flexBoxLayoutWidth
        } else {
            availableWidth
        }

        flexBoxLayout.layout(
            widthPointer - innerPadding,
            heightPointer,
            flexBoxLayoutWidth,
            heightPointer + flexBoxLayout.measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val userNameView = getChildAt(1)
        val textMessageView = getChildAt(2)

        val left = (getChildAt(0).measuredWidth + innerPadding) + paddingLeft
        val top = paddingTop.toFloat()
        val right = (left + Integer.max(
            userNameView.measuredWidth,
            textMessageView.measuredWidth
        ) + 3 * innerPadding) - paddingRight
        val bottom =
            (top + userNameView.measuredHeight + 2 * innerPadding + textMessageView.measuredHeight - paddingBottom)

        canvas.drawRoundRect(
            left.toFloat(),
            top,
            right.toFloat(),
            bottom,
            DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS,
            backgroundPaint
        )
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString(DEFAULT_USER_NAME_KEY, userName)
        bundle.putString(DEFAULT_MESSAGE_TEXT_KEY, textMessage)
        bundle.putParcelable(INSTANCESTATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        userName = bundle.getString(DEFAULT_USER_NAME_KEY).toString()
        setTextMessage(bundle.getString(DEFAULT_MESSAGE_TEXT_KEY).toString())
        super.onRestoreInstanceState(bundle.getParcelable(INSTANCESTATE_KEY))
        invalidate()
    }

    override fun getFlexBoxLayoutChild(): FlexBoxLayout {
        return getChildAt(3) as FlexBoxLayout
    }

    override fun getTextMessageChild(): TextView {
        return getChildAt(2) as TextView
    }

    fun setUserName(newUserName: String) {
        userName = newUserName
        requestLayout()
    }

    companion object {
        const val DEFAULT_USER_NAME = "Default user name"
        const val DEFAULT_MESSAGE_TEXT_KEY = "key_for_message_text"
        const val DEFAULT_USER_NAME_KEY = "key_for_user_name"
        const val INSTANCESTATE_KEY = "instanceState"
    }
}