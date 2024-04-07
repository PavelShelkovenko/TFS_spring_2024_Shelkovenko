package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import androidx.recyclerview.widget.DiffUtil
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User

class UserDiffCallback: DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem

}