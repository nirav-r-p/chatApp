package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.database.UserAuth
import com.example.chatapp.navigationComponent.SetupNavGraph
import com.example.chatapp.ui.theme.ChatAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private  var entry:String="auth"
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
                    SetupNavGraph(navController = navController,entry)
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val auth=UserAuth()
        if (auth.isLoggedIn()){
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