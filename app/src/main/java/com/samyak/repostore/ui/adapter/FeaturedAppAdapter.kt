package com.samyak.repostore.ui.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samyak.repostore.R
import com.samyak.repostore.data.model.AppItem
import com.samyak.repostore.data.model.AppTag
import com.samyak.repostore.databinding.ItemFeaturedAppBinding
import java.util.Locale
import kotlin.random.Random

class FeaturedAppAdapter(
    private val onItemClick: (AppItem) -> Unit
) : ListAdapter<AppItem, FeaturedAppAdapter.FeaturedViewHolder>(AppDiffCallback()) {

    private val gradientColors = listOf(
        intArrayOf(0xFF667eea.toInt(), 0xFF764ba2.toInt()),
        intArrayOf(0xFF11998e.toInt(), 0xFF38ef7d.toInt()),
        intArrayOf(0xFFfc4a1a.toInt(), 0xFFf7b733.toInt()),
        intArrayOf(0xFF4568DC.toInt(), 0xFFB06AB3.toInt()),
        intArrayOf(0xFF0052D4.toInt(), 0xFF6FB1FC.toInt()),
        intArrayOf(0xFFee0979.toInt(), 0xFFff6a00.toInt())
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedViewHolder {
        val binding = ItemFeaturedAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeaturedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturedViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class FeaturedViewHolder(
        private val binding: ItemFeaturedAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(item: AppItem, position: Int) {
            val repo = item.repo

            // Set gradient background
            val colors = gradientColors[position % gradientColors.size]
            val gradient = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                colors
            )
            gradient.cornerRadius = 0f
            binding.gradientBg.background = gradient

            binding.apply {
                tvAppName.text = repo.name
                tvDeveloper.text = repo.owner.login
                tvStars.text = formatNumber(repo.stars)
                tvLanguage.text = repo.language ?: "Code"

                // Tag
                when (item.tag) {
                    AppTag.NEW -> {
                        chipTag.visibility = View.VISIBLE
                        chipTag.text = "NEW"
                        chipTag.setChipBackgroundColorResource(R.color.tag_new)
                    }
                    AppTag.UPDATED -> {
                        chipTag.visibility = View.VISIBLE
                        chipTag.text = "UPDATED"
                        chipTag.setChipBackgroundColorResource(R.color.tag_updated)
                    }
                    AppTag.ARCHIVED -> {
                        chipTag.visibility = View.VISIBLE
                        chipTag.text = "ARCHIVED"
                        chipTag.setChipBackgroundColorResource(R.color.tag_archived)
                    }
                    null -> chipTag.visibility = View.GONE
                }

                Glide.with(ivAppIcon)
                    .load(repo.owner.avatarUrl)
                    .placeholder(R.drawable.ic_app_placeholder)
                    .into(ivAppIcon)
            }
        }

        private fun formatNumber(number: Int): String {
            return when {
                number >= 1_000_000 -> String.format(Locale.US, "%.1fM", number / 1_000_000.0)
                number >= 1_000 -> String.format(Locale.US, "%.1fK", number / 1_000.0)
                else -> number.toString()
            }
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppItem>() {
        override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem) =
            oldItem.repo.id == newItem.repo.id

        override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem) =
            oldItem == newItem
    }
}
