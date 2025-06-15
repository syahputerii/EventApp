package com.syahna.appdicodingevent.detail

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.syahna.appdicodingevent.R
import com.syahna.appdicodingevent.data.local.entity.EventEntity
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(this)
    }
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)?.toIntOrNull()
        if (eventId != null) {
            detailViewModel.getDetailEvent(eventId)
            setupFavoriteButton(eventId.toString())
        } else {
            Log.e("DetailActivity", "Event ID not found")
        }

        detailViewModel.detailEvent.observe(this) { event ->
            event?.let { populateEventData(it) } ?: Log.e("DetailActivity", "Empty data event")
        }

        binding.fabFavorite.setOnClickListener {
            toggleFavorite(eventId)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailActivity, R.color.blue)))
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.ic_arrow_back_white_24
                )
            )
            title = "Detail Event"
        }
    }

    private fun setupFavoriteButton(idEvent: String) {
        detailViewModel.getFavoriteEvent(idEvent).observe(this) { event ->
            isFavorite = event != null
            binding.fabFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_red_24 else R.drawable.ic_favorite_border_24
            )
        }
    }

    private fun populateEventData(event: ListEventsItem) {
        binding.apply {
            eventName.text = HtmlCompat.fromHtml(
                event.name.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventDescription.text = HtmlCompat.fromHtml(
                event.description.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventCategory.text = HtmlCompat.fromHtml(
                event.category.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventOwner.text = HtmlCompat.fromHtml(
                event.ownerName.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventCity.text = HtmlCompat.fromHtml(
                event.cityName.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventSummary.text = HtmlCompat.fromHtml(
                event.summary.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            val remainingQuota = (event.quota ?: 0) - (event.registrants ?: 0)
            eventQuota.text = "Sisa Kuota: $remainingQuota"

            eventBeginTime.text = HtmlCompat.fromHtml(
                event.beginTime.orEmpty(),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            linkButton.setOnClickListener {
                openEventLink(event.link)
            }

            Glide.with(this@DetailActivity)
                .load(event.mediaCover)
                .into(imageView)
        }
    }

    private fun toggleFavorite(eventId: Int?) {
        eventId?.let {
            if (isFavorite) {
                detailViewModel.deleteFavoriteById(it.toString())
                Log.d("DetailActivity", "Removed from favorites: $it")
            } else {
                val event = detailViewModel.detailEvent.value
                if (event?.name != null && event.mediaCover != null) {
                    val newFavorite = EventEntity(
                        id = it.toString(),
                        name = event.name,
                        imageLogo = event.mediaCover
                    )
                    detailViewModel.insertFavorite(newFavorite)
                    Log.d("DetailActivity", "Added to favorites: $it")
                }
            }
            isFavorite = !isFavorite
            binding.fabFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_red_24 else R.drawable.ic_favorite_border_24
            )
        }
    }

    private fun openEventLink(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url.orEmpty())
        }
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}