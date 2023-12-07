package com.example.chatapp.databaseSchema


import com.google.gson.Gson

class UserInfo {
     var userName: String? =null
     var email: String? =null
    var pic: String? =null
    var uid: String? =null
    var tag:String?=null
     var number:String?=null
     var date:String?=null
    constructor() {}
    constructor(
        userName: String,
        email: String="",
        pic:String="",
        uid:String,
        tag:String="~ New User",
        phoneNumber:String="",
        createdAt:String=""
    ){
        this.email=email
        this.userName=userName
        this.pic=pic
        this.uid=uid
        this.tag=tag
        this.number=phoneNumber
        this.date=createdAt
    }


    override fun toString(): String {

        return this.uid + this.userName + this.email
    }

    fun toJsonData():String{
        return Gson().toJson(this)
    }
}

class Contacts{
    var fullname: String? =null
    var uid: String? =null
    constructor(){}
    constructor(userName: String,uid: String){
        this.fullname=userName
        this.uid=uid
    }

}


