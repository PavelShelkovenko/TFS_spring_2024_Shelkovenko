package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.message_group

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.Reaction
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.AddButtonView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.EmojiReactionView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.custom_views.FlexBoxLayout
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.toDp

abstract class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    protected val innerPadding = DEFAULT_INNER_PADDING.toDp(context).toInt()

    protected var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    var textMessage = DEFAULT_MESSAGE_TEXT
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
        onEmojiClick: (Reaction) -> Unit,
        onAddIconClick: () -> Unit,
    ) {
        val flexBoxLayout = getFlexBoxLayoutChild()
        flexBoxLayout.removeAllViews()
        if (newReactionList.isNotEmpty()) {
            newReactionList.forEach { reaction ->
                val newEmojiView = EmojiReactionView(context)
                val emojiCode = reaction.emojiCode
                val count = reaction.count
                with(newEmojiView) {
                    setEmojiCode(emojiCode)
                    setReactionCount(count.toString())
                    setUnselectedBackgroundColor(
                        ResourcesCompat.getColor(resources, R.color.gray, null)
                    )
                    setSelectedBackgroundColor(
                        ResourcesCompat.getColor(resources, R.color.light_gray, null)
                    )
                    setTextColor(
                        ResourcesCompat.getColor(resources, R.color.white, null)
                    )
                    isSelectedReaction = (reaction.userId == localUserId)
                    setOnClickListener {
                        onEmojiClick(reaction)
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
        const val DEFAULT_INNER_PADDING = 8f
        const val DEFAULT_MESSAGE_TEXT = "Default text message"
    }

}