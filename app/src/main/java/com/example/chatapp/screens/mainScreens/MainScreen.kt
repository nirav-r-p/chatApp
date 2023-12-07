package com.example.chatapp.screens.mainScreens


import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.chatapp.ChatActivity
import com.example.chatapp.component.ContactCard
import com.example.chatapp.component.StatusInfo

import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mAuth
import com.example.chatapp.data.network.FirebaseUserOperation.UserAuth.mDbRef
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.contectContenerShapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.validation.formatChatTimestamp
import com.example.chatapp.viewModels.ContactListState
import com.example.chatapp.viewModels.UserViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    userViewModel: UserViewModel,
    context: Context
) {

    val contactListState by userViewModel.homeState.collectAsState(initial = ContactListState())
    val user  by userViewModel.contactListsState.observeAsState()
    val contexts = LocalContext.current

//    val status by chatViewModel.status.observeAsState(initial = "Online")
    val loginUser by userViewModel.logInUser.observeAsState()
    val net by userViewModel.net.observeAsState()


    mDbRef.child(
        mAuth.currentUser?.uid.toString()).child("chats").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chats = mutableMapOf<String, UserMessage>()
                for (chatShot in snapshot.children){
                    val key =chatShot.key?.split("-")
                    Log.d("key", "onDataChange: $key")
                    if(key?.size!! >1) {
                        val chatInfo = chatShot.child("chatInfo").getValue(UserMessage::class.java)
                        val status=chatShot.child("Status").getValue(String::class.java)
                        Log.d("chatInfo", "onDataChange: $chatInfo")
                        chatInfo?.let {
                            chats.put(key[1], chatInfo)
                        }
                    }
                }
               userViewModel.updateChat(chats)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    )




    Scaffold (
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.InviteUser.route)
                    },
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription ="" )
                }
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.ProfileScreen.route)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription ="" )
                }
            }
        }
    ){
        padding->

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, top = 18.dp, end = 14.dp)
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Hi, ${loginUser?.userName}",
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                        )
                        IconButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = modifier.size(30.dp)
                            )
                        }
                    }
                    Text(
                        text = "Story's".uppercase(Locale.getDefault()),
                        fontSize = 13.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray,
                        letterSpacing = 3.2.sp
                    )
                }
                loginUser?.let { user?.userInfo?.let { it1 -> Show(listContacts = it1, it) } }
                Box(modifier = modifier.height(25.dp))
                Box(
                    modifier = modifier
                        .background(Color.White, shape = contectContenerShapes.large)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Log.d("lists chat", "HomeScreen: ${contactListState.contactList}")

                        LazyColumn(
                            modifier = modifier
                                .padding(horizontal = 5.dp, vertical = 30.dp)
                                .fillMaxWidth()
                        ) {
                            items(contactListState.contactList) {
                                contact ->
                                ContactCard(
                                    user = contact.user,
                                    lastMessage =  contact.lastMessage,
                                    lastChatTime = if (contact.lastChatTime.isNotBlank()) formatChatTimestamp(
                                        contact.lastChatTime
                                    ) else "",
                                    numberOfMessage = contact.numberOfMessage
                                ) {
                                    val objectString= contact.user.toJsonData()
                                    val intent=Intent(context,ChatActivity::class.java)
                                    intent.putExtra("user",objectString)
                                    startActivity(context,intent,null)
                                }
                            }
                        }

                }
            }
    }
}

@Composable
fun Show(
    listContacts:List<UserInfo>,
    loginUser: UserInfo
) {
    LazyRow(modifier = Modifier
        .wrapContentHeight(align = Alignment.Top)
        .padding(top = 8.dp),
        userScrollEnabled = true){
        item {
            StatusInfo(name = "Me" , image = loginUser.pic.toString())
        }
        items(items=listContacts){
                cont->
                StatusInfo(cont.pic.toString(), cont.userName.toString(), true)
        }
    }
}
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun HomeScreenPreview() {
//    HomeScreen(modifier = Modifier, navController = rememberNavController())
}
