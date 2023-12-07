package com.example.chatapp.navigationComponent




import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.screens.mainScreens.HomeScreen
import com.example.chatapp.screens.mainScreens.InviteUser
import com.example.chatapp.screens.mainScreens.ProfileSetting
import com.example.chatapp.viewModels.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    context:Context,
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
                HomeScreen(modifier = Modifier,navController=navController, userViewModel = userViewModel, context = context)
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