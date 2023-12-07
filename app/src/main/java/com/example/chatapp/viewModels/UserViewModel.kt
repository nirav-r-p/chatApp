package com.example.chatapp.viewModels

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.network.ContactLists

import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mAuth
import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mDbRef
import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.storage
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage
import com.example.chatapp.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
):ViewModel() {


    private val _contactListState= MutableLiveData<ContactState>()
    val contactListsState:LiveData<ContactState> get() =_contactListState

    private val _homeState = MutableStateFlow(ContactListState())
    val homeState=_homeState.asSharedFlow()

    private val _logInUser=MutableLiveData<UserInfo>()
    val logInUser:LiveData<UserInfo> get() = _logInUser

    private val _loadingImage= MutableLiveData<Boolean>()
    val loadingImage:LiveData<Boolean> get() = _loadingImage

    private val _net =MutableLiveData<Boolean>()
    val net:LiveData<Boolean> get() = _net

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNet(value:Boolean){
        _net.value=value
        viewModelScope.launch {
            userRepository.set(value)
            getContact()
            loginUserInfo()
        }


    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error
//    init {
//        getContact()
//        loginUserInfo()
//    }
    fun logoutUser(){
        try {
            val auth = Firebase.auth
            auth.signOut()
        }catch (e:Exception){
            Log.d("logOut", "logoutUser: ${e.message}")
        }

    }
    private fun loginUserInfo(){
        viewModelScope.launch {
            userRepository.getLoginUserInfo().collect{
                _logInUser.value=it
            }
        }
    }
     fun getContact(){
        viewModelScope.launch {
                userRepository.getContactId().collect {
                    _contactListState.value= _contactListState.value?.copy(uidName = it)
                    Log.d("Contact Main", "getContact: $it")
                    userRepository.getContactList(it).collect {list->
                        _contactListState.value= _contactListState.value?.copy(userInfo = list)
                        Log.d("List Main", "getContact: $list")
                    }
                }
            Log.d("main State", "getContact: ${contactListsState.value?.userInfo} ${contactListsState.value?.userLastMessage}")
            upDateUI()
        }
        viewModelScope.launch {
            userRepository.updateMassage().collect {
                Log.d("chat ", "getContact: $it")
                _contactListState.value= ContactState(userLastMessage = it)
            }
        }
         // Replace with your actual suspend function

    }

   private fun upDateUI(){
       val list= contactListsState.value?.let { combineDetails(it.userInfo,it.userLastMessage) }
       if (list != null) {
           if (list.isNotEmpty())
               Log.d("response list", "upDateUI: $list")
           _homeState.update {
               it.copy(
                   contactList = list
               )
           }
       }


   }

    fun updateChat(chats:Map<String,UserMessage>){
        _contactListState.value=_contactListState.value?.copy(userLastMessage = chats)
        upDateUI()
    }

    fun addContact(user:String,email:String){}


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
data class ContactViewCard(
   val user : UserInfo,
    val lastMessage :String,
    val lastChatTime : String,
    val numberOfMessage :Int
)
fun combineDetails(userInfo: List<UserInfo>,chatInfo:Map<String,UserMessage>):List<ContactViewCard>{
    val list=mutableListOf<ContactViewCard>()
    userInfo.forEach {
        user->
        Log.d("user", "combineDetails: ${user.userName} ${chatInfo[user.uid]}")
          val chatCart= chatInfo[user.uid]?.let {
              ContactViewCard(
                  user = user,
                  lastMessage = it.lastMessage.toString(),
                  lastChatTime = it.lastTime.toString(),
                  numberOfMessage = it.unReadMessage?.toInt() ?:0
              )
          }
        if (chatCart != null) {
            list.add(chatCart)
        }
    }
    return list
}

data class ContactListState(
    val contactList: List<ContactViewCard> = emptyList()
)
data class ContactState(
    val uidName:List<ContactLists> = emptyList(),
    val userInfo: List<UserInfo> = emptyList(),
    val userLastMessage:Map<String,UserMessage> = emptyMap()
)


class MyViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}