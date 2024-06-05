package org.ibda.myguessgame

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<TaskInfo>,
    private val action: String,
    private val fragment:Fragment) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val actionButton: Button = itemView.findViewById(R.id.action_button)

        init {
            // Set click listener for the button
            actionButton.setOnClickListener {
                val task = tasks[adapterPosition]
                Log.d("TaskAdapter", "Action button clicked for task: ${task.id}")

                val direction = when (fragment) {
                    is NormalFragment -> NormalFragmentDirections.actionNormalFragmentToDetailsFragment(task.id)
                    is UrgentFragment -> UrgentFragmentDirections.actionUrgentFragmentToDetailsFragment(task.id)
                    is ImportantFragment -> ImportantFragmentDirections.actionImportantFragmentToDetailsFragment(task.id)
                    else -> throw IllegalArgumentException("Unsupported fragment type")
                }
                // Use findNavController from the Fragment
                fragment.findNavController().navigate(direction)
            }
        }

}


override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTitle.text = task.title
        holder.taskDescription.text = task.description
        holder.actionButton.text = action
    }

    override fun getItemCount() = tasks.size
}