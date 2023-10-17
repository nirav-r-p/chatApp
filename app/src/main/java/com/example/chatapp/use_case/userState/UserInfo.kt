package com.example.chatapp.use_case.userState

class UserInfo {
     var userName: String? =null
     var email: String? =null
    var pic: String? =null
    var uid: String? =null
    constructor() {}
    constructor(userName: String,email: String,pic:String,uid:String){
        this.email=email
        this.userName=userName
        this.pic=pic
        this.uid=uid
    }



    override fun toString(): String {

        return this.uid + this.userName
    }
}
