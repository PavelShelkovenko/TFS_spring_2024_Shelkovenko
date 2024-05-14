package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.screens

import com.kaspersky.kaspresso.screens.KScreen
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.MainActivity
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView

object MainActivityScreen: KScreen<MainActivityScreen>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewClass: Class<*> = MainActivity::class.java

    val bottomNavigationView = KBottomNavigationView { withId(R.id.bottomNavigation) }
}