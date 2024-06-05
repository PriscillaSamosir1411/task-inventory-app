package org.ibda.myguessgame

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.Locale

class DetailsViewModel : ViewModel() {
    private lateinit var retrofit: Retrofit
    private lateinit var taskApiService: TaskApiService

    val TaskMessage = MutableLiveData<Boolean>()

    val taskResult = MutableLiveData<TaskInfo?>()
    val destination = MutableLiveData<String>("")
    val error = MutableLiveData<String>()

    val titleDetail = MutableLiveData<String>("")
    val descriptionDetail = MutableLiveData<String>("")
    val categoryDetail = MutableLiveData<String>("")
    val statusDetail = MutableLiveData<String>("")
    val createdTimeDetail = MutableLiveData<String>("")
    val finishTimeDetail = MutableLiveData<String>("")
    val durationDetail = MutableLiveData<String>("")

    val actionText = MutableLiveData<String>("")

    init {
        this.retrofit = Retrofit.Builder()
            .baseUrl("https://ultimate-anchovy-teaching.ngrok-free.app")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        this.taskApiService = this.retrofit.create(TaskApiService::class.java)
    }

    fun taskDetail(taskId: Int) {
        Log.d("DetailsViewModel", "Fetching details for task ID: $taskId")
        val call = taskApiService.detailTask(taskId)
        call.enqueue(object : Callback<TaskInfo> {
            override fun onResponse(call: Call<TaskInfo>, response: Response<TaskInfo>) {
                Log.d("DetailsViewModel", "Response received: $response")

                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body()
                        val responseBodyString = responseBody?.toString()
                        Log.d("DetailsViewModel", "Raw response body: $responseBodyString")

                        val taskApiResponse = response.body()
                        Log.d("DetailsViewModel", "Parsed response body: $taskApiResponse")

                        if (taskApiResponse != null) {
                            // Process the response here
                            val task = taskApiResponse
                            // Update LiveData for each detail
                            Log.d("DetailsViewModel", "BERHASIL MASUK HANYA JA GA NAMPIL")
                            titleDetail.value = task.title
                            descriptionDetail.value = task.description
                            categoryDetail.value = task.category
                            statusDetail.value = task.status
                            createdTimeDetail.value = task.created_time.toString()
                            finishTimeDetail.value =
                                task.finished_time?.toString() ?: "Not finished yet"
                            durationDetail.value =
                                task.duration?.toString() ?: "Not finished yet"

                            actionText.value = when (task.status) {
                                "New" -> "Take"
                                "In Progress" -> "Done"
                                "Done" -> "Details"
                                else -> "Details"
                            }

                        } else {
                            error.postValue("Empty response")
                            Log.e("DetailsViewModel", "Empty response")
                        }
                    } catch (e: Exception) {
                        error.postValue("Error parsing response: ${e.message}")
                        Log.e("DetailsViewModel", "Error parsing response", e)
                    }
                } else {
                    error.postValue(
                        "Response not successful: ${
                            response.errorBody()?.string()
                        }"
                    )
                    Log.e(
                        "DetailsViewModel",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<TaskInfo>, t: Throwable) {
                error.postValue("Request failed: ${t.message}")
                Log.e("DetailsViewModel", "Request failed", t)
            }
        })
    }

    fun editStatus(taskId: Int) {
        val call = taskApiService.updateStatus(taskId)
        call.enqueue(object : Callback<TaskMessage> {
            override fun onResponse(call: Call<TaskMessage>, response: Response<TaskMessage>) {
                if (response.isSuccessful) {
                    TaskMessage.value = true
                } else {
                    TaskMessage.value = false
                }
            }

            override fun onFailure(call: Call<TaskMessage>, t: Throwable) {
                TaskMessage.value = false
            }
        })
    }

    fun goBack() {
        this.destination.value = "Back"
    }
}

