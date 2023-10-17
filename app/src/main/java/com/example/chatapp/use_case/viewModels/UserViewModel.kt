package com.example.chatapp.use_case.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.use_case.userState.UserInfo
import com.example.chatapp.use_case.userState.UserMessageState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class UserViewModel:ViewModel() {

    var mAuth = FirebaseAuth.getInstance()
    private var mDbRef = FirebaseDatabase.getInstance().getReference("user")
    private val _userList = MutableLiveData<List<UserInfo>>()
    val userList: LiveData<List<UserInfo>> get() = _userList
    private val _users = MutableLiveData<List<UserMessageState>>()
    val users:LiveData<List<UserMessageState>> get() = _users
    init {
        getUserList()
    }
     fun clearUser(){
         _users.value= emptyList()
     }
    private fun getUserList(){
        mDbRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserInfo>()
                val lus= mutableListOf<UserMessageState>()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.child("userInfo").getValue(UserInfo::class.java)
                    val currentMessage=postSnapshot.child("chats").child("lastMessage").getValue<String>()
                    val currentUnRead=postSnapshot.child("chats").child("unReadMessage").getValue<Int>()
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        Log.d("users0", "onDataChange:  ${currentUser?.userName}")
                        currentUser?.let {
                            users.add(it)
                            Log.d("users", "onDataChange: ${it.uid} ${it.userName}")
                        }

                    }
                }
                Log.d("users", "onDataChange: ${users.forEach { i->i.toString() }}")
                _userList.value=users
                Log.d("usersL", "onDataChange: ${_userList.value}")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("mydb", "onCancelled: 0")
            }
        })
    }
}