package org.ibda.myguessgame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path

class UrgentViewModel : ViewModel() {
    private lateinit var retrofit: Retrofit
    private lateinit var taskApiService: TaskApiService

    val tasks = MutableLiveData<List<TaskInfo>>()
    val destination = MutableLiveData<String>("")

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl("https://ultimate-anchovy-teaching.ngrok-free.app")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        this.taskApiService = this.retrofit.create(TaskApiService::class.java)
    }

    fun setDestination(destination: String) {
        this.destination.value = destination
        getTasksByCategoryAndStatus()
    }

    fun getTasksByCategoryAndStatus() {
        val call = taskApiService.getTasksByCategoryAndStatus("urgent", this.destination.value.toString())
        call.enqueue(object : Callback<List<TaskInfo>> {
            override fun onFailure(call: Call<List<TaskInfo>>, t: Throwable) {
                Log.e("UrgentViewModel", "Failed to get search results by category and status", t)
            }

            override fun onResponse(call: Call<List<TaskInfo>>, response: Response<List<TaskInfo>>) {
                if (response.isSuccessful) {
                    tasks.value = response.body()
                } else {
                    Log.e("UrgentViewModel", "Failed to get results by category and status: ${response.errorBody()?.string()}")
                }
            }
        })
    }

    fun actionText(): String {
        return when (destination.value) {
            "new" -> "Details"
            "in progress" -> "Details"
            else -> "Details"
        }
    }


    fun updateStatus(taskId: Int) {
        val call = taskApiService.updateStatus(taskId)
        call.enqueue(object : Callback<TaskMessage> {
            override fun onFailure(call: Call<TaskMessage>, t: Throwable) {
                Log.e("UrgentViewModel", "Failed to update task status", t)
            }

            override fun onResponse(call: Call<TaskMessage>, response: Response<TaskMessage>) {
                if (response.isSuccessful) {
                    Log.d("UrgentViewModel", "Task status updated successfully")
                    // Refresh task list after updating status
                    getTasksByCategoryAndStatus()
                } else {
                    Log.e("UrgentViewModel", "Failed to update task status: ${response.errorBody()?.string()}")
                }
            }
        })
    }
}