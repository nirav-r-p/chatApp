package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.chatapp.data.local.chatDB.OfflineChatDB
import com.example.chatapp.data.local.userDB.OfflineDataBase
import com.example.chatapp.data.network.NetworkDB
import com.example.chatapp.navigationComponent.MainNavGraph
import com.example.chatapp.repository.UserRepository
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.viewModels.UserViewModel

class HomeActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            OfflineDataBase::class.java,
            "UserOffline.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    private val chatdb by lazy {
        Room.databaseBuilder(
            applicationContext,
            OfflineChatDB::class.java,
            "ChatOfflineDB.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    private val userViewModel by viewModels<UserViewModel>(
        factoryProducer = {
            object :ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return UserViewModel(UserRepository(NetworkDB(),db, chatdb)) as T
                }
            }
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

         val networkInfo = connectivityManager.activeNetworkInfo
        setContent {
            ChatAppTheme{

                // A surface container using the 'background' color from the theme
                userViewModel.setNet(networkInfo != null && networkInfo.isConnected)
                Log.d("Chats Screen", "onCreate: Call Chat")
                 navController= rememberNavController()
                  MainNavGraph(navController = navController,userViewModel = userViewModel, context = this){
                      val i=Intent(this,MainActivity::class.java)
                      startActivity(i)
                      finish()
                  }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        val isNet=networkInfo != null && networkInfo.isConnected
        if (!isNet) {
            userViewModel.getContact()
        }
//        else{
//            userViewModel.setNet(true)
//        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatAppTheme {
//       ChatScreen(modifier = Modifier, viewModel = ChatViewModel())
    }
}