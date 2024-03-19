package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.roundToInt

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
        inflate(context, R.layout.message_view_group, this)
    }

    private val defaultBitmap = ResourcesCompat.getDrawable(
        resources,
        R.drawable.ic_launcher_foreground,
        null
    )?.toBitmap()
    var userName = DEFAULT_USER_NAME
        private set
    var textMessage = DEFAULT_MESSAGE_TEXT
        private set
    var userAvatar: Bitmap = defaultBitmap ?: throw IllegalArgumentException("Drawable not found")
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
        val flexBoxLayout = getChildAt(3)

        userNameView.text = userName
        textMessageView.text = textMessage
        try {
            userAvatarView.setImageBitmap(userAvatar)
        } catch (ex: Exception) {
            Log.e("MessageViewGroup", ex.message.toString())
        }


        val innerPadding = 10f.toDp(context).toInt()
        val availableForUserAvatarWidth = (widthSize * 0.2f).roundToInt()
        val availableForUserAvatarHeight =
            availableForUserAvatarWidth    // Чтобы картинка была в ровном квадрате
        val userAvatarWidthSpec =
            MeasureSpec.makeMeasureSpec(availableForUserAvatarWidth, MeasureSpec.AT_MOST)
        val userAvatarHeightSpec =
            MeasureSpec.makeMeasureSpec(availableForUserAvatarHeight, MeasureSpec.AT_MOST)
        measureChild(userAvatarView, userAvatarWidthSpec, userAvatarHeightSpec)

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
        ) + innerPadding
        val childrenHeight =
            userNameView.measuredHeight + textMessageView.measuredHeight + flexBoxLayout.measuredHeight

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> paddingStart + paddingEnd + widthSize
            MeasureSpec.AT_MOST -> paddingStart + paddingEnd + childrenWidth
            MeasureSpec.UNSPECIFIED -> paddingStart + paddingEnd + childrenWidth
            else -> error("Unreachable")
        }

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

        val innerPadding = 10f.toDp(context).toInt()
        val userAvatarView = getChildAt(0)
        val userNameView = getChildAt(1)
        val textMessageView = getChildAt(2)
        val flexBoxLayout = getChildAt(3)

        userAvatarView.layout(
            widthPointer,
            heightPointer,
            widthPointer + userAvatarView.measuredWidth,
            heightPointer + userAvatarView.measuredHeight
        )

        widthPointer += userAvatarView.measuredWidth + innerPadding

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
            widthPointer,
            heightPointer,
            flexBoxLayoutWidth,
            heightPointer + flexBoxLayout.measuredHeight
        )
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putString(DEFAULT_USER_NAME_KEY, userName)
        bundle.putString(DEFAULT_MESSAGE_TEXT_KEY, textMessage)
        bundle.putByteArray(DEFAULT_USER_AVATAR_KEY, bitmapToByteArray(userAvatar))
        bundle.putParcelable(INSTANCESTATE_KEY, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        userName = bundle.getString(DEFAULT_USER_NAME_KEY).toString()
        textMessage = bundle.getString(DEFAULT_MESSAGE_TEXT_KEY).toString()
        val userAvatarByteArray = bundle.getByteArray(DEFAULT_USER_AVATAR_KEY)
        if (userAvatarByteArray != null) {
            userAvatar = BitmapFactory.decodeByteArray(userAvatarByteArray, 0, userAvatarByteArray.size)
        }
        super.onRestoreInstanceState(bundle.getParcelable(INSTANCESTATE_KEY))
        invalidate()
    }

    fun setUserName(newUserName: String) {
        userName = newUserName
        requestLayout()
    }

    fun setTextMessage(newTextMessage: String) {
        textMessage = newTextMessage
        requestLayout()
    }

    fun setUserAvatar(newUserAvatar: Bitmap) {
        userAvatar = newUserAvatar
        requestLayout()
    }

    companion object {
        const val DEFAULT_USER_NAME = "Default user name"
        const val DEFAULT_MESSAGE_TEXT = "Default text message"
        const val DEFAULT_MESSAGE_TEXT_KEY = "key_for_message_text"
        const val DEFAULT_USER_NAME_KEY = "key_for_user_name"
        const val DEFAULT_USER_AVATAR_KEY = "key_for_user_avatar"
        const val INSTANCESTATE_KEY = "instanceState"
    }
}