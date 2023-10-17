package com.example.chatapp.use_case.viewModels

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.use_case.MessageModel

import com.example.chatapp.use_case.userState.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class ChatViewModel() :ViewModel(){

    private val myDBRef=FirebaseDatabase.getInstance().reference
    private val senderId=FirebaseAuth.getInstance().currentUser?.uid
    private val _recName = MutableLiveData<UserInfo>()
    val recName:LiveData<UserInfo> get()=_recName
    private var _messages = MutableLiveData<List<MessageModel>>()
    val messages: LiveData<List<MessageModel>> get() = _messages

    fun setRecName(user:UserInfo){
        _recName.value=user
    }
    private val _status=MutableLiveData<String>()
    val status:LiveData<String> get() = _status

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(recId:String, message:String){
        val senderRoom= "$senderId-$recId"
        val receiverRoom= "$recId-$senderId"
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = currentDateTime.format(formatter)
        val messageObject = MessageModel(message = message,recId,currentTime)
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnCompleteListener {

                myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("chatInfo").child("lastMessage").setValue(message)
                myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("chatInfo").child("lastTime").setValue(currentTime)
                myDBRef.child("user").child(recId!!).child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject).addOnCompleteListener {
                        clearUnRead(recId)
                        myDBRef.child("user").child(recId!!).child("chats").child(receiverRoom!!).child("chatInfo").child("unReadMessage").get().addOnSuccessListener {
                            dataSnapshot->
                            val unReadMessage = dataSnapshot.getValue(Int::class.java)
                            if (unReadMessage != null) {
                                // Handle the unReadMessage value here
                                var unRead:Int =unReadMessage
                                myDBRef.child("user").child(recId!!).child("chats").child(receiverRoom!!).child("chatInfo").child("unReadMessage").setValue(unRead+1)
                                // The value is retrieved as unReadMessage and can be Long, Int, or any other data type based on what you have in the database
                            } else {
                                // Handle the case where the value is null
                                myDBRef.child("user").child(recId!!).child("chats").child(receiverRoom!!).child("chatInfo").child("unReadMessage").setValue(1)
                            }
                       }

                        myDBRef.child("user").child(recId!!).child("chats").child(receiverRoom!!).child("chatInfo").child("lastMessage").setValue(message)
                        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("chatInfo").child("lastTime").setValue(currentTime)

                    }
            }

    }
    fun clearUnRead(recId: String){
        val senderRoom= "$senderId-$recId"
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("chatInfo").child("unReadMessage").setValue(0)
    }
    fun setMessage(meassage:List<MessageModel>){
        _messages.value=meassage
    }
    fun getMessages(recId:String){
        val senderRoom= "$senderId-$recId"
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                var messageLists= mutableListOf<MessageModel>()
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageLists.clear()
                    var count = 0
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(MessageModel::class.java)
                        messageLists.add(message!!)
                        count++
                        Log.d("Messages", "$count")
                    }
                    _messages.value=messageLists
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    fun setStatus(recId: String,bool:String){
         val senderRoom= "$senderId-$recId"
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("Status").setValue(bool)

    }
   fun getStatus(s:String){
       _status.value=s
   }
}



