package com.syahna.appdicodingevent.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syahna.appdicodingevent.di.Injection
import com.syahna.appdicodingevent.repository.DetailRepository
import com.syahna.appdicodingevent.repository.FavoriteRepository

class DetailViewModelFactory(
    private val detailRepository: DetailRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(detailRepository, favoriteRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null

        fun getInstance(context: Context): DetailViewModelFactory {
            return instance ?: synchronized(this) {
                instance?.let { return it }
                DetailViewModelFactory(
                    Injection.provideDetailRepository(),
                    Injection.provideFavoriteRepository(context)
                ).also { instance = it }
            }
        }
    }
}