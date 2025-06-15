package com.syahna.appdicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import com.syahna.appdicodingevent.data.response.EventResponse
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig

class HomeRepository private constructor() {

    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> = _event

    private val _finish = MutableLiveData<List<ListEventsItem>>()
    val finish: LiveData<List<ListEventsItem>> = _finish

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadFinishedEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getFinishedEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _event.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    _errorMessage.value = "Error retrieving data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error occurred: ${t.message}"
            }
        })
    }

    fun loadUpcomingEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getUpcomingEvent().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _finish.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to retrieve data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message}"
            }
        })
    }

    companion object {
        @Volatile
        private var instance: HomeRepository? = null

        fun getInstance(): HomeRepository {
            return instance ?: synchronized(this) {
                HomeRepository().also { instance = it }
            }
        }
    }
}