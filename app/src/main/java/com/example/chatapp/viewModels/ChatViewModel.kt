package com.example.chatapp.viewModels


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.databaseSchema.MessageModel
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ChatViewModel(
  private val chatRepository: ChatRepository
) :ViewModel(){

    private val myDBRef=FirebaseDatabase.getInstance().reference
    private val senderId=FirebaseAuth.getInstance().currentUser?.uid
    private val _recName = MutableLiveData<UserInfo>()
    val recName:LiveData<UserInfo> get()=_recName
    private var _messages = MutableLiveData<List<MessageModel>>()
    val messages: LiveData<List<MessageModel>> get() = _messages

    fun setupChat(user: UserInfo){
        _recName.value=user


    }
    private val _status=MutableLiveData<String>()
    val status:LiveData<String> get() = _status
    val _net=MutableLiveData<Boolean>()
    val net:LiveData<Boolean> get() = _net



    fun setNet(boolean: Boolean){
        _net.value=boolean
        net.value?.let { chatRepository.setNetAvailable(it) }
        getMessages(recName.value?.uid.toString())
        clearUnRead(recName.value?.uid.toString())


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(recId:String, message:String){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = currentDateTime.format(formatter)

        viewModelScope.launch {
            try {
                chatRepository.addMessage(recId,message, time = currentTime)
                getMessages(recId)
            }catch (_:Exception){

            }

        }

    }
    private fun clearUnRead(recId: String){
        chatRepository.clearUnReadCount(recId)
    }
    fun setMessage(message:List<MessageModel>){
        _messages.value=message
    }
    private fun getMessages(recId:String){

        viewModelScope.launch {
            chatRepository.getMessage(recId).collect{
                _messages.value=it
            }
        }
    }

    //This Logic for User Status /- Online,Offline or Typing
    fun setStatus(recId: String,status:String){
         val senderRoom= "$senderId-$recId"
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom).child("Status").setValue(status)
    }
    fun setTyping(recId: String,status:String="Typing.."){
        val recRoom= "$recId-$senderId"
        myDBRef.child("user").child(recId).child("chats").child(recRoom).child("chatInfo").child("lastMessage").setValue(status)
    }

    //set View Model variable to update UI part
   fun setUserStatus(s:String){
       _status.value=s
   }

}



