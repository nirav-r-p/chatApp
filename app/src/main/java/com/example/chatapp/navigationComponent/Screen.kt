package com.example.chatapp.navigationComponent
const val Key1="image"
const val Key2="Name"
sealed class Screen(val route:String){
    object Home: Screen(route = "home_screen")
    object Chat: Screen(
        route = "Chat Screen/{$Key2}/{$Key1}"
    ){
        fun getUserImage(
            id:Int,
            name:String
        ):String{
            return "Chat Screen/$name/$id"
        }

    }
    object LandingPage:Screen(route = "landingPage")
    object SignInScreen:Screen(route = "signInScreen")
    object LoginScreen:Screen(route = "loginScreen")
}
