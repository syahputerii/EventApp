package com.syahna.appdicodingevent.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syahna.appdicodingevent.data.response.DetailResponse
import retrofit2.*
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig

class DetailRepository private constructor() {
    private val _detailEvent = MutableLiveData<ListEventsItem?>()
    val detailEvent: LiveData<ListEventsItem?> = _detailEvent

    fun getDetailEvent(idEvent: Int) {
        Log.d("DetailRepository", "Requesting details for event ID: $idEvent") // Tambahkan log ini
        val client = ApiConfig.getApiService().getDetailEvent(idEvent.toString())
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    val event = response.body()?.event
                    _detailEvent.value = event?.let {
                        ListEventsItem(
                            id = it.id ?: 0,
                            name = it.name ?: "",
                            imageLogo = it.imageLogo ?: "",
                            beginTime = it.beginTime ?: "",
                            category = it.category ?: "",
                            cityName = it.cityName ?: "",
                            description = it.description ?: "",
                            endTime = it.endTime ?: "",
                            link = it.link ?: "",
                            mediaCover = it.mediaCover ?: "",
                            ownerName = it.ownerName ?: "",
                            quota = it.quota ?: 0,
                            registrants = it.registrants ?: 0,
                            summary = it.summary ?: ""
                        )
                    }
                    Log.d("DetailRepository", "Received event details for ID: ${_detailEvent.value?.id}")
                } else {
                    _detailEvent.value = null
                    Log.e("DetailRepository", "Failed to get event details: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _detailEvent.value = null
                Log.e("DetailRepository", "Error fetching event details: ${t.message}")
            }
        })
    }

    companion object {
        @Volatile
        private var instance: DetailRepository? = null
        fun getInstance(): DetailRepository =
            instance ?: synchronized(this) {
                instance ?: DetailRepository()
            }.also {
                instance = it
            }
    }
}