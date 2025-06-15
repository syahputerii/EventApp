package com.syahna.appdicodingevent.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.databinding.ItemVerticalBinding

class EventVertical(
    private var events: List<ListEventsItem>,
    private val onItemClick: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventVertical.EventViewHolder>() {

    inner class EventViewHolder(
        private val binding: ItemVerticalBinding,
        private val clickListener: (ListEventsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {
            with(binding) {
                tvItemName.text = HtmlCompat.fromHtml(
                    event.name.orEmpty(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                Glide.with(root.context)
                    .load(event.imageLogo)
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    clickListener(event)
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
        val binding = ItemVerticalBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size
}