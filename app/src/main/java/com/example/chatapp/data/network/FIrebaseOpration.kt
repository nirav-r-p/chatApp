package com.example.chatapp.data.network


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mAuth
import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mDbRef
import com.example.chatapp.databaseSchema.Contacts
import com.example.chatapp.databaseSchema.MessageModel
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface FirebaseUserOperation {
    object UserAuth{
         val storage= FirebaseStorage.getInstance()
         var mAuth = FirebaseAuth.getInstance()
         var mDbRef = FirebaseDatabase.getInstance().getReference("user")
    }
     suspend fun getUser(userEmail: String):UserInfo
//    fun addContact(user:String,email:String):Resource<String>

     suspend fun  getContacts():Flow<List<ContactLists>>

    fun uploadImage(url: Uri, filename:String)

    suspend fun getContactInfo(contactLists: List<ContactLists>):Flow<List<UserInfo>>

    suspend fun  getContactChat():Flow<Map<String,UserMessage>>

    suspend fun getLoginUser():Flow<UserInfo>

}

class NetworkDB :FirebaseChatOperation(), FirebaseUserOperation{
    private val currentUserUid= mAuth.currentUser?.uid
    private val dbRef= mDbRef
    override suspend fun getUser(userEmail: String): UserInfo {
        val users = mutableListOf<UserInfo>()
        try {
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val currentUser =
                            postSnapshot.child("userInfo").getValue(UserInfo::class.java)
                        if (mAuth.currentUser?.uid != currentUser?.uid) {
                            Log.d("users0", "onDataChange:  ${currentUser?.userName}")
                            currentUser?.let {
                                users.add(it)
                                Log.d("users", "onDataChange: ${it.uid} ${it.userName}")
                            }
                        }
                    }
                    Log.d("users", "onDataChange: ${users.forEach { i -> i.toString() }}")
                }
                override fun onCancelled(error: DatabaseError) {
                    users.clear()
                }
            })
        }catch (_:Exception){

        }
        return UserInfo()
    }

    override suspend fun getContactInfo(contactLists: List<ContactLists>): Flow<List<UserInfo>> = callbackFlow  {
        val users = mutableListOf<UserInfo>()

        for(i in contactLists){
            getValue(i.uid).collect{
                users.add(it)
                Log.d("FB User", "getContactInfo: $it")
            }


        }
        // Emit the list of users
        Log.d("Firebase", "getContactInfo: $users")
        trySend(users)
        close()


       awaitClose {

        }
    }
    private fun getValue(id:String):Flow<UserInfo> = callbackFlow{
     val valueEvent = object : ValueEventListener {
         override fun onDataChange(snapshot: DataSnapshot) {
             val user = snapshot.getValue(UserInfo::class.java)
             if (user != null) {
                 trySend(user)
             }
             Log.d("Fire base User", "onDataChange: $user")
             close()
         }

         override fun onCancelled(error: DatabaseError) {
             // Handle the error or close the flow
             close(error.toException())
         }
     }
        mDbRef.child(id).child("userInfo").addListenerForSingleValueEvent(valueEvent)
        awaitClose {
          mDbRef.child(id).child("userInfo").removeEventListener(valueEvent)
        }
    }
    override suspend fun getContacts():Flow<List<ContactLists>> = callbackFlow{
        val contactList= mutableListOf<ContactLists>()
        val ref= mDbRef.child(currentUserUid.toString())
        val valueEvent= object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    Log.d("data value", "onDataChange: ${data.value}")
                    val uid=data.child("uid").getValue(String::class.java)
                    val name=data.child("fullName").getValue(String::class.java)
                    if (uid!=null) {
                        contactList.add(
                            ContactLists(
                                uid = uid,
                                name = "name"
                            )
                        )
                    }
                    Log.d("get data", "onDataChange: $uid $name")
                }
                trySend(contactList)
                close()
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

        }

        ref.child("Contact").addListenerForSingleValueEvent(valueEvent)
        awaitClose {
            ref.child("Contact").removeEventListener(valueEvent)
        }
    }

    override fun uploadImage(url: Uri, filename: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getContactChat():Flow<Map<String,UserMessage>> = callbackFlow{
        val mapValue = mutableMapOf<String, UserMessage>()
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (sp in snapshot.children) {
                    val key = sp.key?.split("-")
                    val message = sp.child("chatInfo").getValue(UserMessage::class.java)
                    if (key?.size!! > 1 && message != null) {
                        Log.d("Firebase", "onDataChange: $key $message")
                        mapValue[key[1]] = message
                    }
                }
                trySend(mapValue)
                close()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error or log it as needed
                close(error.toException())
            }
        }

        mDbRef.child(currentUserUid.toString()).child("chats").addListenerForSingleValueEvent(valueEvent)

        awaitClose {
            mDbRef.child(currentUserUid.toString()).child("chats").removeEventListener(valueEvent)
        }
    }

    override suspend fun getLoginUser(): Flow<UserInfo> = callbackFlow{
        val valueEvent=object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue(UserInfo::class.java)
                if (user!=null){
                    trySend(user)
                    close()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                close()
            }

        }
        mDbRef.child(currentUserUid.toString()).child("userInfo").addListenerForSingleValueEvent(valueEvent)
        awaitClose {
            mDbRef.child(currentUserUid.toString()).child("userInfo").removeEventListener(valueEvent)
        }
    }


}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    // We'll wrap our data in this 'Success'
    // class in case of success response from api
    class Success<T>(data: T) : Resource<T>(data = data)

    // We'll pass error message wrapped in this 'Error'
    // class to the UI in case of failure response
    class Error<T>(errorMessage: String) : Resource<T>(message = errorMessage)

    // We'll just pass object of this Loading
    // class, just before making an api call
    class Loading<T> : Resource<T>()
}

data class ContactLists(
    val uid:String,
    val name:String
)



//    override fun addContact(user: String, email: String) :Resource<String>{
//        val userInfo= userList.find { it.email==email } ?: return Resource.Error("No Such Account")
//        val contact= userInfo.uid?.let { Contacts(userName = user,uid= it) }
//        try {
//            mDbRef.child(mAuth.currentUser?.uid.toString()).child("Contact").push().setValue(contact)
//        }catch (_:Exception){
//
//        }
//        return Resource.Success("Add SuccessFully")
//    }



open class FirebaseChatOperation{
    private val senderId= mAuth.currentUser?.uid
     suspend fun getMessage(recId:String):Flow<List<MessageModel>> = callbackFlow{
        val senderRoom = "$senderId-$recId"

        val valueEvent=object : ValueEventListener {
            var messageLists= mutableListOf<MessageModel>()
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageLists.clear()

                for (postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(MessageModel::class.java)
                    messageLists.add(message!!)
                }
                trySend(messageLists)
                close()
                if (messageLists.size==1){
                    val mDbRef = FirebaseDatabase.getInstance().getReference("user")
                    val contact= senderId?.let { Contacts(userName = "",uid= it) }
                    mDbRef.child(recId).child("Contact").push().setValue(contact)
                    close()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        mDbRef.child(senderId!!).child("chats").child(senderRoom).child("messages")
            .addListenerForSingleValueEvent(valueEvent)
        awaitClose {
            mDbRef.child(senderId).child("chats").child(senderRoom).child("messages")
                .removeEventListener(valueEvent)
        }

    }

     fun clearUnReadCount(recId: String){
        val senderRoom= "$senderId-$recId"
        mDbRef.child(senderId!!).child("chats").child(senderRoom).child("chatInfo").child("unReadMessage").setValue(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(recId: String, message:String,currentDateTime:String){
        val senderRoom= "$senderId-$recId"
        val receiverRoom= "$recId-$senderId"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = currentDateTime.format(formatter)
        Log.d("message", "addMessage: $message")
        val messageObject = MessageModel(message =message,recId,currentTime)
        mDbRef.child(senderId!!).child("chats").child(senderRoom).child("messages").push()
            .setValue(messageObject).addOnCompleteListener {
                mDbRef.child(senderId).child("chats").child(senderRoom).child("chatInfo").child("lastMessage").setValue(message)
                mDbRef.child(senderId).child("chats").child(senderRoom).child("chatInfo").child("lastTime").setValue(currentTime)
                mDbRef.child(recId).child("chats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject).addOnCompleteListener {
                        clearUnReadCount(recId)
                        mDbRef.child(recId).child("chats").child(receiverRoom).child("chatInfo").child("unReadMessage").get().addOnSuccessListener {
                                dataSnapshot->
                            val unReadMessage = dataSnapshot.getValue(Int::class.java)
                            if (unReadMessage != null) {
                                // Handle the unReadMessage value here
                                val unRead:Int =unReadMessage
                                mDbRef.child(recId).child("chats").child(receiverRoom).child("chatInfo").child("unReadMessage").setValue(unRead+1)
                                // The value is retrieved as unReadMessage and can be Long, Int, or any other data type based on what you have in the database
                            } else {
                                // Handle the case where the value is null
                                mDbRef.child(recId).child("chats").child(receiverRoom).child("chatInfo").child("unReadMessage").setValue(1)
                            }
                        }
                        mDbRef.child(recId).child("chats").child(receiverRoom).child("chatInfo").child("lastMessage").setValue(message)
                        mDbRef.child(senderId).child("chats").child(senderRoom).child("chatInfo").child("lastTime").setValue(currentTime)
                    }
            }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updatePendingMessage(mapValue:Map<String,List<MessageModel>>):Flow<Boolean> = callbackFlow{
        try {
            for ((key,value) in mapValue){
                value.forEach {
                    addMessage(key, it.message!!,it.sendTime!!)
                }
            }
            trySend(true)
        }catch (e:Exception){
            close(e)
        }
        awaitClose {

        }
    }

}