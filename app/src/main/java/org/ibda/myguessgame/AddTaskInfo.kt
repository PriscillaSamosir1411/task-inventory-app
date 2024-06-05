package org.ibda.myguessgame
import com.google.gson.annotations.SerializedName

data class AddTaskInfo(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String
)
