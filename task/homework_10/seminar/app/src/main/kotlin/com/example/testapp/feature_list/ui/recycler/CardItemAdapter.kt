package com.example.testapp.feature_list.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.testapp.databinding.ItemCardBinding
import kotlin.math.max

class CardItemAdapter(
    private var items: List<CardItem> = emptyList(),
    private val onClick: (String) -> Unit
) : Adapter<CardItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardItemViewHolder(binding.root, onClick)
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<CardItem>) {
        max(itemCount, newItems.size).let { count ->
            items = newItems
            notifyItemRangeChanged(0, count)
        }
    }
}
