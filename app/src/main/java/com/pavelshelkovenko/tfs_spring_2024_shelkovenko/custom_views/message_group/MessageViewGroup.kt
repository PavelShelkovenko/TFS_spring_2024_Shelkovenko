package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.message_group

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.AddButtonView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.EmojiReactionView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.FlexBoxLayout
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.toDp

abstract class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    protected val innerPadding = 8f.toDp(context).toInt()

    protected var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    protected var textMessage = DEFAULT_MESSAGE_TEXT
        private set

    fun setTextMessage(newTextMessage: String) {
        textMessage = newTextMessage
        requestLayout()
    }

    abstract fun getFlexBoxLayoutChild(): FlexBoxLayout
    abstract fun getTextMessageChild(): TextView

    fun setReactionList(
        localUserId: Int?,
        newReactionList: List<Reaction>,
        onEmojiClick: (String) -> Unit,
        onAddIconClick: () -> Unit,
    ) {
        val flexBoxLayout = getFlexBoxLayoutChild()
        flexBoxLayout.removeAllViews()
        if (newReactionList.isNotEmpty()) {
            newReactionList.forEach { reaction ->
                val newEmojiView = EmojiReactionView(context)
                val emoji = reaction.emojiCode
                val count = reaction.count
                with(newEmojiView) {
                    setEmojiCode(emoji)
                    setReactionCount(count.toString())
                    setUnselectedBackgroundColor(resources.getColor(R.color.gray))
                    setSelectedBackgroundColor(resources.getColor(R.color.light_gray))
                    setTextColor(resources.getColor(R.color.white))
                    isSelectedReaction = (reaction.userId == localUserId)
                    setOnClickListener {
                        onEmojiClick(emoji)
                    }
                }
                flexBoxLayout.addView(newEmojiView, flexBoxLayout.childCount)
            }
            val addButton = AddButtonView(context)
            addButton.setOnClickListener {
                onAddIconClick()
            }
            flexBoxLayout.addView(addButton, flexBoxLayout.childCount)
        }
    }

    companion object {
        const val DEFAULT_CORNER_RADIUS = 55f
        const val DEFAULT_MESSAGE_TEXT = "Default text message"
    }

}