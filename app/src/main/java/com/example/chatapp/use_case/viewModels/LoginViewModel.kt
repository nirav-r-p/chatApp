package com.example.chatapp.use_case.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.use_case.userState.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.Error

class LoginViewModel: ViewModel() {
    private val _loginEvent= MutableSharedFlow<LoginEvent>()
    val loginEvent=_loginEvent.asSharedFlow()
    private val _uiLoadingState= MutableLiveData<UiLoadingState>()
    val loadingState:LiveData<UiLoadingState>
        get() = _uiLoadingState
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        _uiLoadingState.value=UiLoadingState.IsLoading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiLoadingState.value=UiLoadingState.IsNotLoading
                    onResult(true, null)
                    viewModelScope.launch {
                        _loginEvent.emit(LoginEvent.Success)
                    }
                } else {
                    _uiLoadingState.value=UiLoadingState.IsNotLoading
                    onResult(false, task.exception?.message)
                    viewModelScope.launch {
                        _loginEvent.emit(LoginEvent.ErrorLogin(task.exception?.message ?:"Unknown error"))
                    }
                }
            }
    }
    fun signUpUser(name:String,email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val auth = Firebase.auth
        _uiLoadingState.value=UiLoadingState.IsLoading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiLoadingState.value=UiLoadingState.IsNotLoading
                    onResult(true, null)
                    addUserToDB(name,email,auth.currentUser?.uid!!)
                    viewModelScope.launch {
                        _loginEvent.emit(LoginEvent.Success)
                    }
                } else {
                    _uiLoadingState.value=UiLoadingState.IsNotLoading
                    onResult(false, task.exception?.message)
                    viewModelScope.launch {
                        _loginEvent.emit(LoginEvent.ErrorLogin(task.exception?.message ?:"Unknown error"))
                    }
                }
            }
    }
    private fun addUserToDB(name:String, email:String, uid:String){
        val mDRef= FirebaseDatabase.getInstance().reference
        mDRef.child("user").child(uid).child("userInfo").setValue(UserInfo(userName = name,email=email, uid =uid,pic=""))
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
    sealed class LoginEvent{
        data class ErrorLogin(val error: String):LoginEvent()
        object Success:LoginEvent()
    }
    sealed class UiLoadingState{
        object IsLoading:UiLoadingState()
        object IsNotLoading:UiLoadingState()
    }
}