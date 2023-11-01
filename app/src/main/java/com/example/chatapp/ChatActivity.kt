package com.example.chatapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.navigationComponent.MainNavGraph
import com.example.chatapp.screens.mainScreens.ChatScreen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.viewModels.ChatViewModel
import com.example.chatapp.viewModels.UserViewModel

class ChatActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val chatViewModel: ChatViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatAppTheme{
                // A surface container using the 'background' color from the theme
                 navController= rememberNavController()
                  MainNavGraph(navController = navController,userViewModel = userViewModel , chatViewModel =chatViewModel ){
                      val i=Intent(this,MainActivity::class.java)
                      startActivity(i)
                      finish()
                  }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatAppTheme {
       ChatScreen(modifier = Modifier, viewModel = ChatViewModel(), navController = rememberNavController())
    }
}