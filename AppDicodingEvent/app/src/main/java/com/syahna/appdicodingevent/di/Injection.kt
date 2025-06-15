package com.syahna.appdicodingevent.di

import android.content.Context
import com.syahna.appdicodingevent.data.local.room.EventDatabase
import com.syahna.appdicodingevent.repository.DetailRepository
import com.syahna.appdicodingevent.repository.FavoriteRepository
import com.syahna.appdicodingevent.repository.FinishRepository
import com.syahna.appdicodingevent.repository.HomeRepository
import com.syahna.appdicodingevent.repository.UpcomingRepository

object Injection {

    fun provideHomeRepository(): HomeRepository =
        HomeRepository.getInstance()

    fun provideUpcomingRepository(): UpcomingRepository =
        UpcomingRepository.getInstance()

    fun provideFinishRepository(): FinishRepository =
        FinishRepository.getInstance()

    fun provideDetailRepository(): DetailRepository =
        DetailRepository.getInstance()

    fun provideFavoriteRepository(context: Context): FavoriteRepository {
        val dao by lazy { EventDatabase.getInstance(context).eventDao() }
        return FavoriteRepository.getInstance(dao)
    }
}