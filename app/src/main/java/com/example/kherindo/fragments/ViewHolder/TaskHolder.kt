package com.example.kherindo.fragments.ViewHolder

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kherindo.R
import com.example.kherindo.entities.Task
import com.example.kherindo.respositories.TaskRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    //2
    private var view: View = v
    private lateinit var checkbox: CheckBox
    private lateinit var repository: TaskRepository

    //3
    init {
        v.setOnClickListener(this)
        checkbox = v.findViewById<CheckBox>(R.id.remove_task_checkbox)
        repository = TaskRepository()
    }

    //4
    override fun onClick(v: View) {
        Log.d("RecyclerView", "CLICK!")
    }

    fun bind(task: Task) {
        Log.d("task bind: ", task.title ?: "")
        this.view.findViewById<TextView>(R.id.title_textview).text = task.title
        checkbox.setOnClickListener {
            val isTaskDeleted: Boolean =
                repository.deleteTask(FirebaseAuth.getInstance().currentUser.uid, task)

            var taskDeletionMessage = "Task Completed!"
            if (!isTaskDeleted) {
                taskDeletionMessage = "Error. Could not delete task"
            }

            Snackbar.make(view.rootView.rootView, taskDeletionMessage, Snackbar.LENGTH_SHORT)
        }
    }
}