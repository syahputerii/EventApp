package com.syahna.appdicodingevent.ui.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahna.appdicodingevent.R
import com.syahna.appdicodingevent.data.local.entity.EventEntity
import com.syahna.appdicodingevent.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onEventClick: (EventEntity) -> Unit,
    private val onIconFavoriteClick: (EventEntity) -> Unit
) : ListAdapter<EventEntity, FavoriteAdapter.EventViewHolder>(EVENT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = getItem(position)
        holder.bind(currentEvent)
        holder.itemView.setOnClickListener { onEventClick(currentEvent) }
        holder.binding.ivFavoriteIcon.setOnClickListener { onIconFavoriteClick(currentEvent) }
    }

    class EventViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.tvNameFavorite.text = event.name

            Glide.with(itemView.context)
                .load(event.imageLogo)
                .placeholder(R.drawable.ic_placeholder_24)
                .error(R.drawable.ic_error_24)
                .into(binding.imgPhotoFavorite)
            Log.d("FavoriteAdapter", "Memuat Gambar URL: ${event.imageLogo}")

            binding.ivFavoriteIcon.setImageResource(R.drawable.ic_favorite_red_24)
        }
    }

    companion object {
        private val EVENT_COMPARATOR = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldEvent: EventEntity, newEvent: EventEntity): Boolean {
                return oldEvent.id == newEvent.id
            }

            override fun areContentsTheSame(oldEvent: EventEntity, newEvent: EventEntity): Boolean {
                return oldEvent == newEvent
            }
        }
    }
}