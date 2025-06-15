package com.syahna.appdicodingevent.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.databinding.ItemHorizontalBinding

class EventHorizontal(
    private var events: List<ListEventsItem>,
    private val onItemClick: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventHorizontal.EventViewHolder>() {

    inner class EventViewHolder(
        private val binding: ItemHorizontalBinding,
        private val clickHandler: (ListEventsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            with(binding) {
                Glide.with(root.context)
                    .load(event.imageLogo)
                    .into(eventPhoto)

                root.setOnClickListener {
                    clickHandler(event)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(newEvents: List<ListEventsItem>?) {
        events = newEvents.orEmpty()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHorizontalBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}