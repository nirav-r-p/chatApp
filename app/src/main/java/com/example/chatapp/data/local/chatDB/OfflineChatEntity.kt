package com.example.chatapp.data.local.chatDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OfflineChatEntity(
    val userId:String,
    val pendingMessage:String,//List of UserMessage
    val allMessage:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
)
