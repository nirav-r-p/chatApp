package com.example.chatapp.database


import com.example.chatapp.use_case.userState.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
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

    fun signUpUser(name:String,email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                    addUserToDB(name,email,auth.currentUser?.uid!!)

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
    private fun addUserToDB(name:String, email:String, uid:String){
        val mDRef=FirebaseDatabase.getInstance().reference
        mDRef.child("user").child(uid).setValue(UserInfo(userName = name,email=email, uid =uid,pic=""))
    }
}