package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.UserItemBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.User
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.models.UserOnlineStatus

class PeopleAdapter(
    private val onUserClickListener: (String) -> Unit
) :
    ListAdapter<User, PeopleAdapter.ViewHolder>(UserDiffCallback()) {

    //var onUserClickListener: ((String) -> Unit)? = null
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

            val backgroundResId = when(user.onlineStatus) {
                UserOnlineStatus.ACTIVE -> R.drawable.green_circle_background
                UserOnlineStatus.IDLE -> R.drawable.orange_circle_background
                UserOnlineStatus.OFFLINE -> R.drawable.gray_circle_background
            }

            with(binding) {
                userAvatarImage.setImageBitmap(user.avatar)
                userOnlineStatus.setBackgroundResource(backgroundResId)
                userName.text = user.name
                userEmail.text = user.email
                userItemContainer.setOnClickListener {
                    onUserClickListener.invoke(user.name)
                }
            }
        }
    }


}