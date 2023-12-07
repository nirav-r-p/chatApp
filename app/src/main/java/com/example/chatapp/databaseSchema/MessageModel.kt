package com.example.chatapp.databaseSchema

import android.util.Log

class MessageModel {
      var message: String? = null
     var senderId: String? = null
     var sendTime:String?=null

     constructor(){}
     constructor(message: String,senderId: String,sendTime:String){
         Log.d("mess", "message:$message ")
         this.message=message
         this.sendTime=sendTime
         this.senderId=senderId
     }
}