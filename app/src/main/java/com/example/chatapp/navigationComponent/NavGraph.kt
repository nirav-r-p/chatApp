package com.example.chatapp.navigationComponent


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.chatapp.screens.ChatScreen
import com.example.chatapp.screens.HomeScreen
import com.example.chatapp.screens.LandingPage
import com.example.chatapp.screens.LoginScreen
import com.example.chatapp.screens.SignInScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(
            route= Screen.Home.route
        ){
            HomeScreen(modifier = Modifier,navController=navController)
        }
        composable(
            route= Screen.Chat.route,
            arguments = listOf(
                navArgument(name = Key2){
                    type=NavType.StringType
                },
                navArgument(Key1){
                    type= NavType.IntType
                }
            )
        ){
           back->
            val argument=back.arguments?.getString(Key2)
            val image= back.arguments?.getInt(Key1)
            if (image != null) {
                ChatScreen(modifier =Modifier, name = argument.toString(), userImage = image.toInt())
            }
        }
    }
}