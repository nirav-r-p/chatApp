package com.example.chatapp.use_case.userState

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel:ViewModel() {
    private val _state= MutableStateFlow(LoginUserInfo())
    val state:StateFlow<LoginUserInfo> = _state.asStateFlow()

    fun setLoginUser(userName:String,email:String){
        _state.update {
            it.copy(
                userName = userName,
                email=email,
            )
        }
    }
}