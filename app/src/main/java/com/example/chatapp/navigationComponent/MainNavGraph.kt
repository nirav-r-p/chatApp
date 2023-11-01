package com.example.chatapp.navigationComponent




import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.screens.mainScreens.ChatScreen
import com.example.chatapp.screens.mainScreens.HomeScreen
import com.example.chatapp.screens.mainScreens.InviteUser
import com.example.chatapp.screens.mainScreens.ProfileSetting
import com.example.chatapp.viewModels.ChatViewModel
import com.example.chatapp.viewModels.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    onLogOut:()->Unit
) {
    NavHost(
        navController = navController,
        startDestination = "chat"
    ){
        navigation(startDestination = Screen.Home.route,route="chat"){
            composable(
                route= Screen.Home.route
            ){
                HomeScreen(modifier = Modifier,navController=navController, userViewModel = userViewModel,chatViewModel)
            }
            composable(
                route = Screen.Chat.route,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            200, easing = LinearEasing
                        )
                    )+ slideInHorizontally(
                        animationSpec = tween(
                            200, easing = FastOutLinearInEasing
                        )
                    )
                }
            ){
                ChatScreen(modifier = Modifier, viewModel= chatViewModel, navController = navController)
            }
            composable(
                route=Screen.ProfileScreen.route
            ){
                ProfileSetting(viewModel = userViewModel, onLogOut = onLogOut, navController = navController)
            }
            composable(
                route=Screen.InviteUser.route
            ){
                InviteUser(userViewModel = userViewModel, navController = navController)
            }
        }
    }
}