package com.example.chatapp.data.local.userDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chatapp.databaseSchema.UserMessage

@Entity
data class OfflineData(
    @PrimaryKey(autoGenerate = true) val id:Int=1,
    val loginUser:String,
    val contactInfo:String,
    val message: String,
    val lastMessage:String,

)
