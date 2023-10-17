package com.example.chatapp.use_case.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.use_case.UserMessage
import com.example.chatapp.use_case.userState.UserInfo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class UserViewModel:ViewModel() {

    var mAuth = FirebaseAuth.getInstance()
     var mDbRef = FirebaseDatabase.getInstance().getReference("user")
    private val _userList = MutableLiveData<List<UserInfo>>()
    val userList: LiveData<List<UserInfo>> get() = _userList

    private val _users = MutableLiveData<MutableMap<String, UserMessage>>()
    val users:LiveData<MutableMap<String, UserMessage>> get() = _users

    private val _loadDataEvent= MutableSharedFlow<LoadContentEvent>()

    val loadDataEvent=_loadDataEvent.asSharedFlow()
    private val _uiLoadingState= MutableLiveData<UiLoadingState>()
    val loadingState:LiveData<UiLoadingState>
        get() = _uiLoadingState
    init {
        getUserList()
    }
     fun clearUser(){
         _userList.value= emptyList()
     }

    private fun getUserList(){
        mDbRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiLoadingState.value=UiLoadingState.IsLoading
                val users = mutableListOf<UserInfo>()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.child("userInfo").getValue(UserInfo::class.java)
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
                viewModelScope.launch {
                    _loadDataEvent.emit(LoadContentEvent.Success)
                }

                Log.d("usersL", "onDataChange: ${_userList.value}")
                _uiLoadingState.value=UiLoadingState.IsNotLoading

            }
            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _loadDataEvent.emit(LoadContentEvent.ErrorLogin("Get User Error$error"))
                }
                _uiLoadingState.value=UiLoadingState.IsNotLoading
                Log.d("mydb", "onCancelled: 0")
            }
        })
    }
     fun getChatInfo( mp:MutableMap<String,UserMessage>){
       _users.value=mp
    }
    sealed class LoadContentEvent{
        data class ErrorLogin(val error: String):LoadContentEvent()
        object Success:LoadContentEvent()
    }
    sealed class UiLoadingState{
        object IsLoading:UiLoadingState()
        object IsNotLoading:UiLoadingState()
    }
}