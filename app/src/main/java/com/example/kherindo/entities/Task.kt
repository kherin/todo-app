package com.example.kherindo.entities

class Task {
    var uid: String? = null
    var userId: String? = null
    var title: String? = null
    var notes: String? = null
    var reminderDate: String? = null

    constructor(){}

     constructor(uid: String? = null,
                 userId: String? = null,
                 title: String? = null,
                 notes: String? = null,
                 reminderDate: String? = null
     ) {
        this.uid = uid
         this.userId = userId
         this.title = title
         this.notes = notes
         this.reminderDate = reminderDate
     }


}