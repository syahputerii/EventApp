package com.syahna.appdicodingevent.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.databinding.ItemRowBinding

class EventAdapter(
    private var events: List<ListEventsItem>,
    private val onItemClick: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            with(binding) {
                tvItemName.text = event.name.orEmpty()

                Glide.with(root.context)
                    .load(event.mediaCover)
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    onItemClick(event)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(newEvents: List<ListEventsItem>) {
        events = newEvents
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}