package com.example.chatapp

import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage

class ContactInfo {
    lateinit  var id:String
    lateinit var status:String
    lateinit var userInfo: UserInfo
    lateinit var message: List<UserMessage>

}