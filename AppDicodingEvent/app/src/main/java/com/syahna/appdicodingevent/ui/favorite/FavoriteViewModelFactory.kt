package com.syahna.appdicodingevent.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syahna.appdicodingevent.di.Injection
import com.syahna.appdicodingevent.repository.FavoriteRepository

class FavoriteViewModelFactory(
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            FavoriteViewModel(favoriteRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null

        fun getInstance(context: Context): FavoriteViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: createFactory(context).also { instance = it }
            }
        }

        private fun createFactory(context: Context): FavoriteViewModelFactory {
            val repository = Injection.provideFavoriteRepository(context)
            return FavoriteViewModelFactory(repository)
        }
    }
}