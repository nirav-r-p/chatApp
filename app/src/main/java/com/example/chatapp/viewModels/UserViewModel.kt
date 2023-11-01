package com.example.chatapp.viewModels

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.databaseSchema.Contacts
import com.example.chatapp.databaseSchema.UserMessage
import com.example.chatapp.databaseSchema.UserInfo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.http.Url
import java.util.Date

class UserViewModel:ViewModel() {
    private val storage=FirebaseStorage.getInstance()
     var mAuth = FirebaseAuth.getInstance()
     var mDbRef = FirebaseDatabase.getInstance().getReference("user")


    private val _userList = MutableLiveData<List<UserInfo>>()
    val userList: LiveData<List<UserInfo>> get() = _userList

    private val _contactList = MutableLiveData<List<UserInfo>>()
    val contactList: LiveData<List<UserInfo>> get() = _contactList
    private val _users = MutableLiveData<MutableMap<String, UserMessage>>()
    val users:LiveData<MutableMap<String, UserMessage>> get() = _users


    private val _loadDataEvent= MutableSharedFlow<LoadContentEvent>()

    val loadDataEvent=_loadDataEvent.asSharedFlow()

    private val _uiLoadingState= MutableLiveData<UiLoadingState>()

    private val _logInUser=MutableLiveData<UserInfo>()
    val logInUser:LiveData<UserInfo> get() = _logInUser

    private val _loadingImage= MutableLiveData<Boolean>()
    val loadingImage:LiveData<Boolean> get() = _loadingImage
    val loadingState:LiveData<UiLoadingState>
        get() = _uiLoadingState
    init {
        getUserList()
    }

    fun logoutUser(){
        _uiLoadingState.value= UiLoadingState.IsLoading
        try {
            val auth = Firebase.auth
            auth.signOut()
            _userList.value= emptyList()
        }catch (e:Exception){
            Log.d("logOut", "logoutUser: ${e.message}")
        }
        _uiLoadingState.value= UiLoadingState.IsNotLoading
    }
    private fun getUserList(){
        _uiLoadingState.value= UiLoadingState.IsLoading
        mDbRef.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserInfo>()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.child("userInfo").getValue(UserInfo::class.java)
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        Log.d("users0", "onDataChange:  ${currentUser?.userName}")
                        currentUser?.let {
                            users.add(it)
                            Log.d("users", "onDataChange: ${it.uid} ${it.userName}")

                        }
                    }else{
                        currentUser.let {
                            _logInUser.value=it
                            Log.d("Date Ct", "onDataChange: ${it?.toString()}")
                        }
                    }
                }
                Log.d("users", "onDataChange: ${users.forEach { i->i.toString() }}")
                _userList.value=users
                viewModelScope.launch {
                    _loadDataEvent.emit(LoadContentEvent.Success)
                }

                Log.d("usersL", "onDataChange: ${_userList.value}")

                // This Part about add Contact When user enter  in app.
                val ref=mDbRef.child(mAuth.currentUser?.uid.toString())
                val  uidList= mutableListOf<String>()
                ref.child("Contact").addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        uidList.clear()
                        for (uidSnap in snapshot.children){
                            val contacts = uidSnap.getValue(Contacts::class.java)
                            if (contacts != null) contacts.uid?.let { uidList.add(it) }
                        }
                        setContact(uidList)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                _uiLoadingState.value= UiLoadingState.IsNotLoading

            }
            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _loadDataEvent.emit(LoadContentEvent.ErrorLogin("Get User Error$error"))
                }
                _uiLoadingState.value= UiLoadingState.IsNotLoading
                Log.d("mdb", "onCancelled: 0")
            }
        })
    }
    fun addContact(user:String,email:String){
      val userInfo= userList.value?.find { it.email==email } ?: return
        val contact= userInfo.uid?.let { Contacts(userName = user,uid= it) }
        mDbRef.child(mAuth.currentUser?.uid.toString()).child("Contact").push().setValue(contact)

    }
     fun setContact(uidList:List<String>){
         val contactUser= mutableListOf<UserInfo>()
         userList.value?.forEach {
             if( it.uid in uidList){
                 contactUser.add(it)
             }
         }
        _contactList.value=contactUser
        _uiLoadingState.value= UiLoadingState.IsNotLoading
    }
     fun getChatInfo( mp:MutableMap<String, UserMessage>){
       _users.value=mp
    }
    private fun uploadUserImage(url: Uri,filename:String){
        val ref= mAuth.currentUser?.let { storage.reference.child("Profile").child(filename)}
        ref?.putFile(url)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        uploadUrl(url.toString())
                    }
                } else {
                    Log.d("error for uploadImage", "uploadUserImage:${it.exception} ")
                }
        }

    }
    private fun uploadUrl(url:String){
       val imageRef=mDbRef.child(mAuth.currentUser?.uid.toString()).child("userInfo")
        imageRef.child("pic").setValue(url).addOnCompleteListener {
            if (it.isSuccessful){
                _loadingImage.value=false
                Log.d("Upload Successfully", "uploadUrl: Done")
            }else{
                Log.d("not Successfully", "uploadUrl: ${it.exception}")
            }
        }
    }
    fun uploadImage(url: Uri){
        _loadingImage.value=true
        val filename= mAuth.currentUser?.let { "profilePhoto"+it.uid }
        if (logInUser.value?.pic.toString().isBlank()){
            uploadUserImage(url,filename.toString())
        }else{
            Log.d("File path", "uploadImage: ${logInUser.value?.pic!!}")
            val deleteRef=  storage.reference.child("Profile").child(filename.toString())
            deleteRef.delete().addOnCompleteListener {
                if (it.isSuccessful){
                    uploadUserImage(url,filename.toString())
                }
                else{
                    Log.d("delete error", "uploadImage: ${it.exception}")
                }
            }

        }

    }
    sealed class LoadContentEvent{
        data class ErrorLogin(val error: String): LoadContentEvent()
        object Success: LoadContentEvent()
    }
    sealed class UiLoadingState{
        object IsLoading: UiLoadingState()
        object IsNotLoading: UiLoadingState()
    }

}