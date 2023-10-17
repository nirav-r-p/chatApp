package com.example.chatapp.navigationComponent


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.screens.mainScreens.ChatScreen
import com.example.chatapp.screens.mainScreens.HomeScreen
import com.example.chatapp.screens.SignLogScreen.LandingPage
import com.example.chatapp.screens.SignLogScreen.LoginScreen
import com.example.chatapp.screens.SignLogScreen.SignInScreen
import com.example.chatapp.use_case.viewModels.ChatViewModel
import com.example.chatapp.use_case.viewModels.LoginViewModel
import com.example.chatapp.use_case.viewModels.UserViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    entryKey:String,
    loginViewModel: LoginViewModel,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {
    NavHost(
        navController = navController,
        startDestination = entryKey
    ){
        navigation(startDestination = Screen.Home.route,route="chat"){
            composable(
                route= Screen.Home.route
            ){
                HomeScreen(modifier = Modifier,navController=navController, userViewModel = userViewModel,chatViewModel)
            }
            composable(
                route = Screen.Chat.route
            ){

                ChatScreen(modifier = Modifier, viewModel= chatViewModel)
            }
        }
        navigation(startDestination = Screen.LandingPage.route, route = "auth"){
           composable(Screen.LandingPage.route){
               LandingPage(modifier = Modifier, navController = navController)
           }
            composable(Screen.LoginScreen.route){
                LoginScreen(modifier = Modifier,navController,loginViewModel)
            }
            composable(Screen.SignInScreen.route){
                SignInScreen(modifier = Modifier, navController,loginViewModel)
            }
        }
    }
}