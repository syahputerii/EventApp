package com.syahna.appdicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.syahna.appdicodingevent.data.response.EventResponse
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig

class HomeViewModel : ViewModel() {

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents: LiveData<List<ListEventsItem>> get() = _listFinishedEvents

    private val _listUpcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val listUpcomingEvents: LiveData<List<ListEventsItem>> get() = _listUpcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val logTag = "HomeViewModel"

    fun fetchFinishedEvents() {
        setLoadingState(true)
        val apiClient = ApiConfig.getApiService().getFinishedEvent()
        apiClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                setLoadingState(false)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val filteredEvents = responseBody.listEvents?.filterNotNull() ?: emptyList()
                        _listFinishedEvents.value = filteredEvents.take(5)
                    }
                } else {
                    Log.e(logTag, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, throwable: Throwable) {
                setLoadingState(false)
                Log.e(logTag, "Failure: ${throwable.message}")
            }
        })
    }

    fun fetchUpcomingEvents() {
        setLoadingState(true)
        val apiClient = ApiConfig.getApiService().getUpcomingEvent()
        apiClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                setLoadingState(false)
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val filteredEvents = responseBody.listEvents?.filterNotNull() ?: emptyList()
                        _listUpcomingEvents.value = filteredEvents.take(5)
                    }
                } else {
                    Log.e(logTag, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, throwable: Throwable) {
                setLoadingState(false)
                Log.e(logTag, "Failure: ${throwable.message}")
            }
        })
    }

    private fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }
}