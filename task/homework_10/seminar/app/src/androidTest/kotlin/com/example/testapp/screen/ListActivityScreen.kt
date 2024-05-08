package com.example.testapp.screen

import android.view.View
import com.example.testapp.R
import com.example.testapp.feature_list.ui.ListActivity
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ListActivityScreen : KScreen<ListActivityScreen>() {

    override val layoutId: Int = R.layout.activity_list
    override val viewClass: Class<*> = ListActivity::class.java

    val button = KButton { withId(R.id.load) }

    val recycler = KRecyclerView(
        builder = { withId(R.id.recycler) },
        itemTypeBuilder = {
            itemType(::KCardItem)
        }
    )

    class KCardItem(parent: Matcher<View>) : KRecyclerItem<KCardItem>(parent) {

        val title = KTextView { withId(R.id.title) }
        val description = KTextView { withId(R.id.description) }
    }
}
