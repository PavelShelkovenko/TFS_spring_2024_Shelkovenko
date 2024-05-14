package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.streams

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels.streams.StreamsInfoFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object StreamInfoScreen: KScreen<StreamInfoScreen>()  {

    override val layoutId: Int = R.layout.fragment_streams_info
    override val viewClass: Class<*> = StreamsInfoFragment::class.java

    val streamsRecycler = KRecyclerView(
        builder = { withId(R.id.streams_info_rv) },
        itemTypeBuilder = {
            itemType(StreamInfoScreen::KStream)
            itemType(StreamInfoScreen::KTopic)
        }
    )

    class KStream(parent: Matcher<View>): KRecyclerItem<KStream>(parent) {
        val streamName = KTextView(parent) { withId(R.id.stream_name) }
        val openTopicsButton = KView(parent) { withId(R.id.open_topics_button) }
    }

    class KTopic(parent: Matcher<View>): KRecyclerItem<KTopic>(parent) {
        val topicName = KTextView(parent) { withId(R.id.topic_name) }
    }
}