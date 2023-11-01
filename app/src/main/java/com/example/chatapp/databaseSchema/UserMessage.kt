package com.example.chatapp.databaseSchema

import android.util.Log

class UserMessage {
    var unReadMessage:Int?=null
     var lastMessage: String?=null
    var lastTime: String?=null


    constructor(){}

    constructor(ls:String,time:String,unRead:Int){
        this.unReadMessage=unRead
        this.lastMessage=ls
        this.lastTime=time

    }

}