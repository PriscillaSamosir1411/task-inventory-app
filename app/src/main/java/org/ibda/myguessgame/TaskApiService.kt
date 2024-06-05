package org.ibda.myguessgame

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.PUT

interface TaskApiService {
    @GET("/tasks")
    fun getTasks(): Call<List<Task>>

    @GET("/tasks/{task_id}")
    fun detailTask(@Path("task_id") id: Int): Call<TaskInfo>

    @PUT("/task_data/{id}")
    fun updateStatus(@Path("id") id: Int): Call<TaskMessage>

    @GET("/tasks")
    fun getTasksByCategoryAndStatus(
        @Query("category") category: String,
        @Query("status") status: String):
            Call<List<TaskInfo>>

    @POST("/tasks/add")
    fun addTask(
        @Body infoTaskAdd: AddTaskInfo): Call<AddTaskInfo>
}