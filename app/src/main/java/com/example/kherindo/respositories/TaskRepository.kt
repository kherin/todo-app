package com.example.kherindo.respositories

import com.example.kherindo.entities.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.*

class TaskRepository {
    private var database: DatabaseReference = Firebase.database.reference

    fun createTask(userId: String, newTask: Task) {
        var uid = newTask.uid
        uid = uid ?: UUID.randomUUID().toString()
        database.child("users").child(userId).child("todos").child(uid).setValue(newTask)
    }

    fun deleteTask(userId: String, deletedTask: Task): Boolean {
        return try {
            var uid = deletedTask.uid
            uid = uid ?: UUID.randomUUID().toString()
            database.child("users").child(userId).child("todos").child(uid).removeValue()
            return true
        } catch (e: Exception) {
            return false
        }

    }

}