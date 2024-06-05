package org.ibda.myguessgame

data class TaskInfo (
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val created_time: String,
    val finished_time: String,
    val duration: String,
    val status: String,
    val isSuccessful: Boolean
) {
    fun message(): String {
        return if (isSuccessful) {
            "Task detail fetched successfully."
        } else {
            "Failed to fetch task detail."
        }
    }

    fun body(): Any {
        return if (isSuccessful) {
            this
        } else {
            // Jika tidak berhasil, Anda dapat mengembalikan pesan error atau objek lain yang sesuai dengan kebutuhan aplikasi Anda
            "Error"
        }
    }
}
