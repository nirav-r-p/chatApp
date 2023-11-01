package com.example.chatapp.navigationComponent


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

import com.example.chatapp.screens.signLogScreen.LandingPage
import com.example.chatapp.screens.signLogScreen.LoginScreen
import com.example.chatapp.screens.signLogScreen.SignInScreen
import com.example.chatapp.viewModels.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    onNavigate:()->Unit
) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ){
        navigation(startDestination = Screen.LandingPage.route, route = "auth"){
           composable(Screen.LandingPage.route){
               LandingPage(modifier = Modifier, navController = navController)
           }
            composable(Screen.LoginScreen.route){
                LoginScreen(modifier = Modifier,navController,loginViewModel,onNavigate)
            }
            composable(Screen.SignInScreen.route){
                SignInScreen(modifier = Modifier, navController,loginViewModel, onNavigate = onNavigate)
            }


        }
    }
}