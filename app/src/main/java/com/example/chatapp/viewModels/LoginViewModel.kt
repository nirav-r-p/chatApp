package com.example.chatapp.viewModels


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.databaseSchema.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LoginViewModel: ViewModel() {
    private val _loginEvent= MutableSharedFlow<LoginEvent>()
    val loginEvent=_loginEvent.asSharedFlow()
    private val _uiLoadingState= MutableLiveData<UiLoadingState>()
    val loadingState:LiveData<UiLoadingState>
        get() = _uiLoadingState
    private val _message=MutableLiveData<String>()
    val message:LiveData<String> get() = _message
    private  val _verify=MutableLiveData<Boolean>()
    val verify:LiveData<Boolean> get() = _verify
   init {
       _message.value=""
   }
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        _uiLoadingState.value= UiLoadingState.IsLoading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified==true){
                        _uiLoadingState.value= UiLoadingState.IsNotLoading
                        _verify.value=true
                        onResult(true, null)
                        viewModelScope.launch {
                            _loginEvent.emit(LoginEvent.Success)
                        }
                    }else{
                          _message.value="Please Verify Your Email Id"
                          _uiLoadingState.value= UiLoadingState.IsNotLoading
                    }

                } else {
                    _uiLoadingState.value= UiLoadingState.IsNotLoading
                    onResult(false, task.exception?.message)
                    viewModelScope.launch {
                        _loginEvent.emit(
                            LoginEvent.ErrorLogin(
                                task.exception?.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun signUpUser(name:String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth

        _uiLoadingState.value= UiLoadingState.IsLoading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                        tk->
                        if (tk.isSuccessful){
                             _message.value="Verify Your Email And LogIn"
                            _uiLoadingState.value= UiLoadingState.IsNotLoading

                            onResult(true, null)
                            addUserToDB(name,email,auth.currentUser?.uid!!)
                            viewModelScope.launch {
                                _loginEvent.emit(LoginEvent.Success)
                            }
                        }else{
                            _uiLoadingState.value= UiLoadingState.IsNotLoading
                            Log.d("Verification Error", "signUpUser:${tk.exception?.message.toString()} ")
                        }
                    }
                    if (auth.currentUser?.isEmailVerified==true){
                        _uiLoadingState.value= UiLoadingState.IsNotLoading
                        _verify.value=true
                        onResult(true, null)
                        viewModelScope.launch {
                            _loginEvent.emit(LoginEvent.Success)
                        }
                    }else{
                        _uiLoadingState.value= UiLoadingState.IsNotLoading
                    }
                } else {
                    _uiLoadingState.value= UiLoadingState.IsNotLoading
                    onResult(false, task.exception?.message)
                    viewModelScope.launch {
                        _loginEvent.emit(
                            LoginEvent.ErrorLogin(
                                task.exception?.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addUserToDB(name:String, email:String, uid:String){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = currentDateTime.format(formatter)
        val mDRef= FirebaseDatabase.getInstance().reference
        mDRef.child("user").child(uid).child("userInfo").setValue(UserInfo(userName = name,email=email,uid=uid, createdAt = currentTime))
    }
    fun logoutUser(){
        val auth = Firebase.auth
        auth.signOut()
    }

    fun isLoggedIn():Boolean{
        val auth = Firebase.auth
        return auth.currentUser!=null && auth.currentUser!!.isEmailVerified
    }
    sealed class LoginEvent{
        data class ErrorLogin(val error: String): LoginEvent()
        object Success: LoginEvent()
    }
    sealed class UiLoadingState{
        object IsLoading: UiLoadingState()
        object IsNotLoading: UiLoadingState()
    }


}