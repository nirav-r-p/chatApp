package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.database.UserAuth
import com.example.chatapp.navigationComponent.SetupNavGraph
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.use_case.viewModels.ChatViewModel
import com.example.chatapp.use_case.viewModels.UserViewModel
import com.example.chatapp.use_case.viewModels.LoginViewModel
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private  var entry:String="auth"

    private val loginViewModel : LoginViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController= rememberNavController()
                    SetupNavGraph(
                        navController = navController,
                        entry,
                        loginViewModel,
                        userViewModel,
                        chatViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (loginViewModel.isLoggedIn()){
           entry="chat"
        }
    }
    
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {
        StatusInfo()
    }
}