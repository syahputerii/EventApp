package com.syahna.appdicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import com.syahna.appdicodingevent.data.response.EventResponse
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig

class UpcomingRepository private constructor() {

    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchUpcomingEvents(query: String? = null) {
        _isLoading.postValue(true)
        val apiRequest = ApiConfig.getApiService().getUpcomingEvent()

        apiRequest.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    _event.postValue(response.body()?.listEvents?.filterNotNull().orEmpty())
                } else {
                    _errorMessage.postValue("Failed to retrieve data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, throwable: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Error: ${throwable.localizedMessage}")
            }
        })
    }

    companion object {
        @Volatile
        private var instance: UpcomingRepository? = null

        fun getInstance(): UpcomingRepository {
            return instance ?: synchronized(this) {
                UpcomingRepository().also { instance = it }
            }
        }
    }
}