package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.chat

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.chat.ChatFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ChatScreen: KScreen<ChatScreen>() {

    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val streamName = KTextView { withId(R.id.stream_name) }
    val topicName = KTextView { withId(R.id.topic_name) }
    val messageField = KEditText { withId(R.id.message_field) }
    val sendMessageButton = KView { withId(R.id.send_message_button) }

    val chatRecycler = KRecyclerView(
        builder = { withId(R.id.chat_recycler_view) },
        itemTypeBuilder = {
            itemType(ChatScreen::KChatDate)
            itemType(ChatScreen::KReceivedMessage)
            itemType(ChatScreen::KSendMessage)
        }
    )

    class KChatDate(parent: Matcher<View>): KRecyclerItem<KChatDate>(parent) {
        val date = KTextView(parent) { withId(R.id.date) }
    }

    class KReceivedMessage(parent: Matcher<View>): KRecyclerItem<KReceivedMessage>(parent) {
        val userAvatar = KImageView(parent) { withId(R.id.received_user_avatar) }
        val message = KTextView(parent) { withId(R.id.received_text_message) }
        val userName = KTextView(parent) { withId(R.id.received_user_name) }
        val flexBox = KFlexBoxLayout(parent) { withId(R.id.received_flex_layout) }
    }

    class KSendMessage(parent: Matcher<View>): KRecyclerItem<KSendMessage>(parent) {
        val message = KTextView(parent) { withId(R.id.send_text_message) }
        val flexBox = KFlexBoxLayout(parent) { withId(R.id.send_flex_layout) }
    }
}