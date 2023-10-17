package com.example.chatapp.use_case

import com.example.chatapp.use_case.MessageModel

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