package com.syahna.appdicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syahna.appdicodingevent.data.local.entity.EventEntity
import com.syahna.appdicodingevent.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    val getIsLoading: LiveData<Boolean> = favoriteRepository.isLoading

    fun getAllFavEvent(): LiveData<List<EventEntity>> {
        return favoriteRepository.getAllFavorites()
    }

    fun deleteFavoriteById(id: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.deleteFavorite(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}