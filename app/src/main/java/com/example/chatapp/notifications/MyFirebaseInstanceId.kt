package com.example.chatapp.notifications

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceId:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val firebaseUser=Firebase.auth.currentUser
        val refreshToken:String?=FirebaseMessaging.getInstance().token.toString()
        if (firebaseUser!=null){
            updateToken(refreshToken)
        }
    }

    private fun updateToken(refreshToken: String?) {
         val firebaseUser=Firebase.auth.currentUser
        val ref=FirebaseDatabase.getInstance().reference.child("Tokens")
        val token = Token(refreshToken!!)
        ref.child(firebaseUser!!.uid).setValue(token)
    }
}

class Token{
    private var token:String=""
    constructor(){}
    constructor(token: String){
        this.token=token
    }
    fun getToken():String{
        return token
    }
    fun setToken(token:String){
        this.token=token
    }
}