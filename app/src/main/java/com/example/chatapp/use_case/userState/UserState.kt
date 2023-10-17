package com.example.chatapp.use_case.userState



data class UserState(
    val loginUser:UserInfo= UserInfo("","","",""),
    val contactList:List<UserInfo> = emptyList()
)


