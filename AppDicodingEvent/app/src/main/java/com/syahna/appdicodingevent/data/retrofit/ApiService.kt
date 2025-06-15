package com.syahna.appdicodingevent.data.retrofit

import com.syahna.appdicodingevent.data.response.DetailResponse
import com.syahna.appdicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvent(
        @Query("active") isActive: Int = 1
    ): Call<EventResponse>

    @GET("events")
    fun getFinishedEvent(
        @Query("active") isActive: Int = 0
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") eventId: String): Call<DetailResponse>
}