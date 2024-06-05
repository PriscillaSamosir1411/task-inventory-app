package org.ibda.myguessgame

class TaskApiResponse (
    val task:List<TaskInfo>
)

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val category: String,
    val created_time: String,
    val finished_time: String
)