package com.syahna.appdicodingevent.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import com.syahna.appdicodingevent.data.response.EventResponse
import com.syahna.appdicodingevent.data.response.ListEventsItem
import com.syahna.appdicodingevent.data.retrofit.ApiConfig

class FinishRepository private constructor() {

    private val _finishEvents = MutableLiveData<List<ListEventsItem>>()
    val finish: LiveData<List<ListEventsItem>> = _finishEvents

    private val _isLoadingStatus = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoadingStatus

    private val _errorStatus = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorStatus

    fun fetchFinishedEvents(query: String? = null) {
        _isLoadingStatus.value = true
        val apiCall = ApiConfig.getApiService().getFinishedEvent()

        apiCall.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoadingStatus.value = false
                if (response.isSuccessful) {
                    _finishEvents.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    _errorStatus.value = "Error retrieving data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoadingStatus.value = false
                _errorStatus.value = "Failure: ${t.message}"
            }
        })
    }

    companion object {
        @Volatile
        private var instance: FinishRepository? = null

        fun getInstance(): FinishRepository {
            return instance ?: synchronized(this) {
                FinishRepository().also { instance = it }
            }
        }
    }
}