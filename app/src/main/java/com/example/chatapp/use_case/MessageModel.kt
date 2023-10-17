package com.example.chatapp.use_case

 class MessageModel {
     var message: String? = null
     var senderId: String? = null
     var sendTime:String?=null

     constructor(){}
     constructor(message: String,senderId: String,sendTime:String){
         this.message=message
         this.sendTime=sendTime
         this.senderId=senderId
     }

}