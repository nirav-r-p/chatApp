package com.example.chatapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.navigationComponent.SetupNavGraph
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.viewModels.LoginViewModel
import com.example.chatapp.viewModels.UserViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    private val loginViewModel : LoginViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ChatAppTheme(
                darkTheme = true,
                statusBarColor = Color(252, 198, 65, 255).toArgb()

            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    navController= rememberNavController()
                    SetupNavGraph(
                        navController = navController,
                        loginViewModel,
                    ){
                        val i=Intent(this,ChatActivity::class.java)
                        startActivity(i)
                        finish()
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (loginViewModel.isLoggedIn()){
            val i=Intent(this,ChatActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {
        StatusInfo("")
    }
}