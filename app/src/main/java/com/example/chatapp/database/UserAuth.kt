package com.example.chatapp.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAuth {

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun signUpUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
   fun logoutUser(){
       val auth = Firebase.auth
       auth.signOut()
   }

    fun isLoggedIn():Boolean{
        val auth = Firebase.auth
        if(auth.currentUser!=null){
            return true;
        }
        return false
    }
}