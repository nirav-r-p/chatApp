package com.example.chatapp.screens.mainScreens

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.util.Log
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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.component.ContactCard
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.databaseSchema.Contacts
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.contectContenerShapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.validation.formatChatTimestamp
import com.example.chatapp.viewModels.ChatViewModel
import com.example.chatapp.viewModels.UserViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
) {
    val userLists by userViewModel.contactList.observeAsState(initial = emptyList())
    val status by chatViewModel.status.observeAsState(initial = "Online")
    val users by userViewModel.users.observeAsState()
    val loginUser by userViewModel.logInUser.observeAsState()

    userViewModel.mDbRef.child(userViewModel.mAuth.currentUser?.uid.toString()).child("chats").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mP = mutableMapOf<String, UserMessage>()
                for (chatShot in snapshot.children){
                    val key =chatShot.key?.split("-")
                    Log.d("key", "onDataChange: $key")
                    if(key?.size!! >1) {
                        val chatInfo = chatShot.child("chatInfo").getValue(UserMessage::class.java)
                        val status=chatShot.child("Status").getValue(String::class.java)
                        Log.d("chatInfo", "onDataChange: $chatInfo")
                        chatInfo?.let {
                            mP.put(key[1], chatInfo)
                        }
                    }
                }
               userViewModel.getChatInfo(mP)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    )
    //when you add new User in contact
    val ref=userViewModel.mDbRef.child(userViewModel.mAuth.currentUser?.uid.toString())
    val  uidList= mutableListOf<String>()
    ref.child("Contact").addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            uidList.clear()
            for (uidSnap in snapshot.children){
                val contacts = uidSnap.getValue(Contacts::class.java)
                if (contacts != null) contacts.uid?.let { uidList.add(it) }
            }
            userViewModel.setContact(uidList)
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })


    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    userViewModel.loadingState.observe(LocalLifecycleOwner.current) { loadingState ->
        isLoading = when (loadingState) {
            UserViewModel.UiLoadingState.IsLoading-> {
                true
            }
            UserViewModel.UiLoadingState.IsNotLoading -> {
                false
            }
        }
    }
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
        if(isLoading){
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center) {
                Box {
                    CircularProgressIndicator()
                }
            }
        } else {
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
                        text = "Story's".toUpperCase(),
                        fontSize = 13.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray,
                        letterSpacing = 3.2.sp
                    )
                }
                loginUser?.let { Show(listContacts = userLists, it) }
                Box(modifier = modifier.height(25.dp))
                Box(
                    modifier = modifier
                        .background(Color.White, shape = contectContenerShapes.large)
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    LazyColumn(
                        modifier = modifier
                            .padding(horizontal = 5.dp, vertical = 30.dp)
                            .fillMaxWidth()
                    ) {
                        items(userLists.size) { index ->
                            val lm =
                                if (users?.get(userLists[index].uid)?.lastMessage== null) "New" else users?.get(
                                    userLists[index].uid
                                )?.lastMessage.toString()
                            val laT =
                                if (users?.get(userLists[index].uid)?.lastTime == null) "" else users?.get(
                                    userLists[index].uid
                                )?.lastTime.toString()
                            val unRe =
                                if (users?.get(userLists[index].uid)?.unReadMessage == null) 0 else users?.get(
                                    userLists[index].uid
                                )?.unReadMessage!!.toInt()
                            ContactCard(
                                user = userLists[index],
                                lastMessage = if (status == "Typing...") "Typing..." else lm,
                                lastChatTime = if (laT.isNotEmpty()) formatChatTimestamp(laT) else "",
                                numberOfMessage = unRe
                            ) {
                                chatViewModel.setRecName(userLists[index])
                                chatViewModel.getMessages(userLists[index].uid.toString())
                                chatViewModel.clearUnRead(userLists[index].uid.toString())
                                chatViewModel.setStatus(recId = userLists[index]
                                    .uid.toString(), "Online")
                                navController.navigate(Screen.Chat.route)
                            }

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
        .padding(top = 8.dp)){
        item {
            StatusInfo(name = "Me" , image = loginUser.pic.toString())
        }
        items(items=listContacts){
                cont->
                StatusInfo(cont.pic.toString(), cont.userName.toString(), true)
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun HomeScreenPreview() {
//    HomeScreen(modifier = Modifier, navController = rememberNavController())
}
