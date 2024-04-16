package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_2

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.EmojiReactionView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.custom_views.FlexBoxLayout
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.DemoActivityBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.ReceivedMessageViewGroupBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.message.reaction.EmojiFactory
import kotlin.random.Random

class DemoActivity : AppCompatActivity() {


    private lateinit var binding: DemoActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DemoActivityBinding.inflate(layoutInflater)
        val messageViewGroupBinding = ReceivedMessageViewGroupBinding.bind(binding.root)
        setContentView(binding.root)
        initClickListeners(messageViewGroupBinding)
    }

    private fun initClickListeners(
        messageViewGroupBinding: ReceivedMessageViewGroupBinding
    ) {

        with(binding) {
            addEmojiButton.setOnClickListener {
                addEmojiView(flexBoxLayout = messageViewGroupBinding.flexLayout)
            }
            setTextMessageButton.setOnClickListener {
               messageGroup.setTextMessage(binding.textMessageEt.text.toString())
            }
            setUserNameButton.setOnClickListener {
               messageGroup.setUserName(binding.userNameEt.text.toString())
            }
            setUserAvatarButton.setOnClickListener {
                changeUserAvatar()
            }
        }
    }

    private fun changeUserAvatar() {
        try {
            val someUserAvatar = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_launcher_background,
                null
            )?.toBitmap() ?: throw IllegalArgumentException("Drawable not found")
           binding.messageGroup.userAvatar.setImageBitmap(someUserAvatar)
        } catch (ex: Exception) {
            Log.e("DemoActivity", ex.message.toString())
        }
    }

    private fun generateEmojiView(parentFlexBoxLayout: FlexBoxLayout): EmojiReactionView {
        val emojiView = EmojiReactionView(context = this)
        emojiView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        emojiView.apply {
            val emoji = EmojiFactory.getEmojiList().shuffled().first()
            val count = Random.nextInt(1, 3)
            this.setEmojiCode(emoji.emojiCode)
            this.setReactionCount(count.toString())
            this.isSelectedReaction = true
            val selectionColor = resources.getColor(R.color.light_blue, null)
            setSelectedBackgroundColor(selectionColor)
            setOnClickListener {
                if (this.reactionCount == "1" && isSelectedReaction) {
                    parentFlexBoxLayout.removeView(this)
                }
                if (!isSelectedReaction) {
                    this.setReactionCount((reactionCount.toInt() + 1).toString())
                } else {
                    this.setReactionCount((reactionCount.toInt() - 1).toString())
                }
                setReactionSelected(!isSelectedReaction)
            }
        }
        return emojiView
    }

    private fun addEmojiView(flexBoxLayout: FlexBoxLayout) {
        val newEmojiView = generateEmojiView(flexBoxLayout)
        if (flexBoxLayout.childCount == 1) {
            flexBoxLayout.addView(newEmojiView, flexBoxLayout.childCount - 1)
        } else {
            var needToAddView = true
            for (i in 0 until flexBoxLayout.childCount - 1) {
                val currentChild = flexBoxLayout.getChildAt(i)
                if ((currentChild as EmojiReactionView).emojiCode == newEmojiView.emojiCode) {
                    currentChild.setReactionCount((currentChild.reactionCount.toInt() + 1).toString())
                    needToAddView = false
                }
            }
            if (needToAddView) {
                flexBoxLayout.addView(newEmojiView, flexBoxLayout.childCount - 1)
            }
        }
    }
}