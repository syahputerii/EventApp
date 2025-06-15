package com.syahna.appdicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import com.syahna.appdicodingevent.data.local.entity.EventEntity
import com.syahna.appdicodingevent.data.local.room.EventDao

class FavoriteRepository private constructor(
    private val eventDao: EventDao
) {
    val isLoading = MutableLiveData<Boolean>()

    fun getFavoriteEvent(id: String): LiveData<EventEntity> = eventDao.getFavoriteEvent(id)

    suspend fun insertFavoriteEvent(event: EventEntity) {
        isLoading.postValue(true)
        withContext(Dispatchers.IO) {
            eventDao.insertFavoriteEvent(event)
        }
        isLoading.postValue(false)
    }

    suspend fun deleteFavorite(id: String) {
        isLoading.postValue(true)
        withContext(Dispatchers.IO) {
            eventDao.deleteFavoriteEvent(id)
        }
        isLoading.postValue(false)
    }

    fun getAllFavorites(): LiveData<List<EventEntity>> = eventDao.getAllFavoriteEvent()

    fun isFavorite(id: String): LiveData<Boolean> = eventDao.isFavorite(id)

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(eventDao: EventDao): FavoriteRepository {
            return instance ?: synchronized(this) {
                instance ?: createInstance(eventDao).also { instance = it }
            }
        }

        private fun createInstance(eventDao: EventDao): FavoriteRepository {
            return FavoriteRepository(eventDao)
        }
    }
}