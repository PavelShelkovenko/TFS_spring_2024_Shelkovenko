package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens.profile.own

import com.kaspersky.kaspresso.screens.KScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.OwnProfileFragment
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object OwnProfileScreen: KScreen<OwnProfileScreen>() {

    override val layoutId: Int = R.layout.fragment_own_profile
    override val viewClass: Class<*> = OwnProfileFragment::class.java


    val avatar = KImageView { withId(R.id.own_user_avatar_image) }
    val userName = KTextView { withId(R.id.own_user_name) }
    val userOnlineStatus = KTextView { withId(R.id.own_user_online_status) }
}