package org.ibda.myguessgame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FormDataViewModel : ViewModel() {
    private val _taskSubmissionStatus = MutableLiveData<String>()
    val taskSubmissionStatus: LiveData<String> get() = _taskSubmissionStatus

    private val taskApiService: TaskApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ultimate-anchovy-teaching.ngrok-free.app") // Ganti dengan URL base API Anda
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        taskApiService = retrofit.create(TaskApiService::class.java)
    }

    fun addTask(addTask: AddTaskInfo) {
        Log.d("FormDataViewModel", "Data to be submitted: $addTask")
        val call: Call<AddTaskInfo> = taskApiService.addTask(addTask)
        call.enqueue(object : Callback<AddTaskInfo> {
            override fun onFailure(call: Call<AddTaskInfo>, t: Throwable) {
                _taskSubmissionStatus.postValue("Failed to create task: ${t.message}")
            }

            override fun onResponse(call: Call<AddTaskInfo>, response: Response<AddTaskInfo>) {
                if (response.isSuccessful) {
                    _taskSubmissionStatus.postValue("Task created successfully: ${response.body()}")
                } else {
                    _taskSubmissionStatus.postValue("Failed to create task: ${response.errorBody()?.string()}")
                }
            }
        })
    }

}
