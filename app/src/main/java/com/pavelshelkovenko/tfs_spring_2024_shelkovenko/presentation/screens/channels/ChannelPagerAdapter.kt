package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ChannelPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: MutableList<Fragment> = mutableListOf()
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
    fun update(fragments: List<Fragment>) {
        this.fragments.clear()
        this.fragments.addAll(fragments)
        notifyDataSetChanged()
    }

    fun getCurrentFragment(position: Int): Fragment? {
        return if (position in fragments.indices) {
            fragments[position]
        } else {
            null
        }
    }
}