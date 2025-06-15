package com.syahna.appdicodingevent.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syahna.appdicodingevent.data.local.entity.EventEntity
import com.syahna.appdicodingevent.repository.DetailRepository
import com.syahna.appdicodingevent.repository.FavoriteRepository
import kotlinx.coroutines.launch


class DetailViewModel(
    private val detailRepository: DetailRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    val detailEvent = detailRepository.detailEvent

    fun getDetailEvent(idEvent: Int) {
        detailRepository.getDetailEvent(idEvent)
    }

    fun getFavoriteEvent(id: String) = favoriteRepository.getFavoriteEvent(id)

    fun insertFavorite(event: EventEntity) {
        if (event.id.isNotEmpty() && event.name.isNotEmpty()) {
            viewModelScope.launch {
                favoriteRepository.insertFavoriteEvent(event)
            }
        }
    }

    fun deleteFavoriteById(id: String) = viewModelScope.launch {
        Log.d("DetailViewModel", "Attempting to delete favorite")
        favoriteRepository.deleteFavorite(id)
        Log.d("DetailViewModel", "Favorite deleted")
    }
}