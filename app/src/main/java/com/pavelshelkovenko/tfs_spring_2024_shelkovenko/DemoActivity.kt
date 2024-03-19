package com.pavelshelkovenko.tfs_spring_2024_shelkovenko

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.DemoActivityBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.MessageViewGroupBinding
import kotlin.random.Random

class DemoActivity : AppCompatActivity() {

    private val emojiList = listOf(
        Emoji("+1", "2764"),
        Emoji("grinning", "1f600"),
        Emoji("smiley", "1f603"),
        Emoji("big_smile", "1f604"),
        Emoji("grinning_face_with_smiling_eyes", "1f601"),
        Emoji("laughing", "1f606"),
        Emoji("sweat_smile", "1f605"),
        Emoji("rolling_on_the_floor_laughing", "1f923"),
        Emoji("joy", "1f602"),
        Emoji("smile", "1f642"),
        Emoji("upside_down", "1f643"),
        Emoji("wink", "1f609"),
        Emoji("blush", "1f60a"),
        Emoji("innocent", "1f607"),
        Emoji("heart_eyes", "1f60d"),
        Emoji("heart_kiss", "1f618"),
        Emoji("kiss", "1f617"),
        Emoji("smiling_face", "263a"),
        Emoji("kiss_with_blush", "1f61a"),
        Emoji("kiss_smiling_eyes", "1f619"),
    )

    private lateinit var binding: DemoActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DemoActivityBinding.inflate(layoutInflater)
        val messageViewGroupBinding = MessageViewGroupBinding.bind(binding.root)
        setContentView(binding.root)
        initClickListeners(messageViewGroupBinding)
    }

    private fun initClickListeners(
        messageViewGroupBinding: MessageViewGroupBinding
    ) {
        with(messageViewGroupBinding) {
            addIcon.setOnClickListener {
                addEmojiView(flexBoxLayout = messageViewGroupBinding.flexLayout)
            }
        }
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
            binding.messageGroup.setUserAvatar(someUserAvatar)
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
            val emoji = emojiList.shuffled().first()
            val count = Random.nextInt(1, 3)
            this.setEmojiCode(emoji.code)
            this.setReactionCount(count.toString())
            this.isSelectedReaction = true
            val selectionColor = resources.getColor(R.color.lightBlue, null)
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