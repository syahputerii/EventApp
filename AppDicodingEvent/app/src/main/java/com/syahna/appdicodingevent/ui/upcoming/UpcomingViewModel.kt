package com.syahna.appdicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syahna.appdicodingevent.data.response.EventResponse
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val logTag = "UpcomingViewModel"

    fun fetchEvents() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService().getUpcomingEvent()
        apiService.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        _listEvents.value = eventResponse.listEvents?.filterNotNull() ?: emptyList()
                    }
                } else {
                    Log.e(logTag, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, throwable: Throwable) {
                _isLoading.value = false
                Log.e(logTag, "Request failed: ${throwable.message}")
            }
        })
    }
}