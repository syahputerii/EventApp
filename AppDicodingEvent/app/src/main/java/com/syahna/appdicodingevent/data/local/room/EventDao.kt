package com.syahna.appdicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.syahna.appdicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: EventEntity): Long

    @Query("SELECT * FROM event WHERE id = :id")
    fun getFavoriteEvent(id: String): LiveData<EventEntity>

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: String)

    @Query("SELECT * FROM event")
    fun getAllFavoriteEvent(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM event WHERE id = :id)")
    fun isFavorite(id: String): LiveData<Boolean>

    @Query("DELETE FROM event")
    suspend fun deleteAllFavorites()
}