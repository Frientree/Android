package com.d101.presentation.collection.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d101.domain.model.JuiceForCollection
import com.d101.presentation.R
import com.d101.presentation.databinding.ItemCollectionBinding

class CollectionAdapter(private val collectionClickListener: ((JuiceForCollection) -> Unit)) :
    ListAdapter<JuiceForCollection, CollectionAdapter.CollectionViewHolder>(diffUtil) {
    class CollectionViewHolder(
        private val binding: ItemCollectionBinding,
        private val itemClickListener: (JuiceForCollection) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(juice: JuiceForCollection) {
            Glide.with(itemView).load(juice.juiceImageUrl).into(binding.juiceImageImageView)

            if (adapterPosition % 2 == 0) {
                binding.collectionBackgroundImageView.setImageResource(
                    R.drawable.back_collection_left,
                )
            } else {
                binding.collectionBackgroundImageView.setImageResource(
                    R.drawable.back_collection_right,
                )
            }

            if (juice.juiceOwn) {
                binding.juiceNameTextView.text = juice.juiceName
                binding.juiceImageImageView.setOnClickListener { itemClickListener(juice) }
                binding.juiceImageImageView.colorFilter = null
            } else {
                binding.juiceNameTextView.text = "???"
                binding.juiceImageImageView.setOnClickListener(null)
                binding.juiceImageImageView.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
            }

            if (juice.juiceNum == -1L) {
                binding.juiceNameTextView.visibility = View.GONE
            } else {
                binding.juiceNameTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding, collectionClickListener)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateList(list: List<JuiceForCollection>) {
        val newList = MutableList<JuiceForCollection>(list.size) { list[it] }
        if (list.size % 2 != 0) {
            newList.add(
                JuiceForCollection(
                    juiceNum = -1L,
                    juiceName = "",
                    juiceDescription = "",
                    juiceImageUrl = "",
                    juiceOwn = false,
                    juiceBackgroundImageUrl = "",
                ),
            )
        }
        submitList(newList)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<JuiceForCollection>() {
            override fun areItemsTheSame(
                oldItem: JuiceForCollection,
                newItem: JuiceForCollection,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: JuiceForCollection,
                newItem: JuiceForCollection,
            ): Boolean {
                return oldItem.juiceNum == newItem.juiceNum
            }
        }
    }
}
