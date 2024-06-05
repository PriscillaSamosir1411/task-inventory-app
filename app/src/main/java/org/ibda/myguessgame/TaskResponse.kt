package org.ibda.myguessgame

import com.squareup.moshi.Json

data class TaskResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "category")
    val category: String,
    @Json(name = "created_time")
    val createdTime: String,
    @Json(name = "finished_time")
    val finishedTime: String?,
    @Json(name = "duration")
    val duration: String?
)

data class TaskMessage(
    val message: String
)
