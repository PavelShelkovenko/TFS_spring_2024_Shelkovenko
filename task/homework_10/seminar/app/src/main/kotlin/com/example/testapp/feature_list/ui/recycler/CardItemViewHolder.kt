package com.example.testapp.feature_list.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.testapp.databinding.ItemCardBinding

class CardItemViewHolder(
    itemView: View,
    private val onClick: (String) -> Unit
) : ViewHolder(itemView) {

    private val binding = ItemCardBinding.bind(itemView)

    fun bind(item: CardItem) = with(binding) {
        title.text = item.title
        description.text = item.description
        root.setOnClickListener { onClick(item.title) }
    }
}
