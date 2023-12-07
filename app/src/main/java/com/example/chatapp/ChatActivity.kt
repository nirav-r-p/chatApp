@file:JvmName("ChatActivityKt")

package com.example.chatapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.chatapp.data.local.chatDB.OfflineChatDB
import com.example.chatapp.data.local.userDB.OfflineDataBase
import com.example.chatapp.data.network.NetworkDB
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.repository.ChatRepository
import com.example.chatapp.repository.UserRepository
import com.example.chatapp.screens.mainScreens.ChatScreen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.viewModels.ChatViewModel
import com.example.chatapp.viewModels.UserViewModel
import com.google.gson.Gson

class ChatActivity : ComponentActivity() {
//    private val chatViewModel : ChatViewModel by viewModels()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            OfflineDataBase::class.java,
            "UserOffline.db"
        )
            .build()
    }
private val chatdb by lazy {
    Room.databaseBuilder(
        applicationContext,
        OfflineChatDB::class.java,
        "ChatOfflineDB.db"
    )
        .build()
}
    private val chatViewModel by viewModels<ChatViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ChatViewModel(ChatRepository(networkDB = NetworkDB(),chatdb,db) ) as T
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
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                val net by chatViewModel.net.observeAsState(true)
                chatViewModel.setNet(networkInfo != null && networkInfo.isConnected)
//                net.let {
//                    chatViewModel.setNet(it)
                    ChatScreen(modifier = Modifier, viewModel = chatViewModel)

//                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val receivedData = intent.getStringExtra("user")
        val userInfo = Gson().fromJson(receivedData, UserInfo::class.java)
        chatViewModel.setupChat(userInfo)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ChatAppTheme {
        Greeting("Android")
    }
}