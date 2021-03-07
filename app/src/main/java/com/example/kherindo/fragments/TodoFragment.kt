package com.example.kherindo.fragments

import NewTodoDialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.kherindo.R
import com.example.kherindo.entities.Task
import com.example.kherindo.fragments.ViewHolder.TaskHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class TodoFragment : Fragment() {

    lateinit var taskRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentUserId: String = FirebaseAuth.getInstance().currentUser.uid

        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(currentUserId)
            .child("todos")

        val options: FirebaseRecyclerOptions<Task> = FirebaseRecyclerOptions.Builder<Task>()
            .setQuery(query, Task::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = instantiateTaskAdapter(options)

        // attach adapter to recycler view
        taskRecyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        taskRecyclerView.adapter = adapter
        taskRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        view.findViewById<FloatingActionButton>(R.id.add_todo_fab).setOnClickListener {

            val addTodoDialog = NewTodoDialogFragment()
            addTodoDialog.show(childFragmentManager, "")
        }

    }

    private fun instantiateTaskAdapter(options: FirebaseRecyclerOptions<Task>): FirebaseRecyclerAdapter<Task, TaskHolder> {
        return object : FirebaseRecyclerAdapter<Task, TaskHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
                return TaskHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_todo, parent, false)
                )
            }

            override fun onBindViewHolder(holder: TaskHolder, position: Int, model: Task) {
                holder.bind(model)
            }
        }
    }
}