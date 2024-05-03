package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.UserItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.setColoredBackgroundStatus

class PeopleAdapter(
    private val onUserClickListener: (Int) -> Unit
) : ListAdapter<User, PeopleAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class ViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                userName.text = user.name
                userEmail.text = user.email
                userOnlineStatus.setColoredBackgroundStatus(user.onlineStatus)
                Glide.with(itemView).load(user.avatarUrl).into(userAvatarImage)
                userItemContainer.setOnClickListener {
                    onUserClickListener.invoke(user.id)
                }
            }
        }
    }


}