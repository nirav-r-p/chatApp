package com.example.chatapp.navigationComponent

sealed class Screen(val route:String){
    object Home: Screen(route = "home_screen")
    object Chat: Screen(route = "ChatScreen")
    object LandingPage:Screen(route = "landingPage")
    object SignInScreen:Screen(route = "signInScreen")
    object LoginScreen:Screen(route = "loginScreen")
    object ProfileScreen:Screen(route = "ProfileSetting")
    object InviteUser:Screen(route = "InviteUser")
}
