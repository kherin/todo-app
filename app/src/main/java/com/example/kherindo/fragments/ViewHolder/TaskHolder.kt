package com.example.kherindo.fragments.ViewHolder

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.kherindo.R
import com.example.kherindo.entities.Task
import com.example.kherindo.respositories.TaskRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class TaskHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
    //2
    private var view: View = v
    private lateinit var checkbox: CheckBox
    private lateinit var repository: TaskRepository
    private lateinit var task: Task

    //3
    init {
        v.setOnClickListener(this)
        checkbox = v.findViewById<CheckBox>(R.id.remove_task_checkbox)
        repository = TaskRepository()
    }

    //4
    override fun onClick(v: View) {
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy")
        val dueDate: String =
            SimpleDateFormat("dd EEEE MMMM HH:mm").format(dateFormat.parse(task.reminderDate))
        val builder = AlertDialog.Builder(v.context)
        builder
            .setTitle("${task.title}")
            .setMessage("${task.notes} due on $dueDate")
            .create()
            .show()
    }

    fun bind(task: Task) {
        this.task = task
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