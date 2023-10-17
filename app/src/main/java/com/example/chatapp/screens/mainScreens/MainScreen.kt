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
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.component.ContactCard
import com.example.chatapp.component.ContactsList
import com.example.chatapp.component.GetInfo
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.database.UserAuth
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.use_case.UserMessage
import com.example.chatapp.use_case.viewModels.ChatViewModel
import com.example.chatapp.use_case.viewModels.LoginViewModel
import com.example.chatapp.use_case.viewModels.UserViewModel
import com.example.chatapp.validation.getHhMM
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {
    val userLists by userViewModel.userList.observeAsState(initial = emptyList())
    val status by chatViewModel.status.observeAsState(initial = "Online")
    val users by userViewModel.users.observeAsState()
    val user=UserAuth()
    val listContacts= GetInfo().getContact()
    userViewModel.mDbRef.child(userViewModel.mAuth.currentUser?.uid.toString()).child("chats").addValueEventListener(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mP = mutableMapOf<String, UserMessage>()
                for (chatShot in snapshot.children){
                    val key =chatShot.key?.split("-")
                    Log.d("key", "onDataChange: $key")
                    if(key?.size!! >1) {

                        val chatInfo = chatShot.child("chatInfo").getValue(UserMessage::class.java)
                        Log.d("chatInfo", "onDataChange: $chatInfo")
                        if (key.isNotEmpty() && chatInfo!=null) {
                            chatInfo.let {
                                mP.put(key[1], chatInfo!!)
                            }
                        }
                    }
//                    when (chatShot.child("Status").value.toString()){
//                        "Typing"->chatViewModel.getStatus("Typing...")
//                        "Online"->chatViewModel.getStatus("Online")
//                        "Offline"->chatViewModel.getStatus("")
//                    }
                }

               userViewModel.getChatInfo(mP)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    )
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
            FloatingActionButton(
                onClick = {
                    user.logoutUser()
                    userViewModel.clearUser()
                    navController.navigate("auth"){
                        popUpTo("chat")
                    }

                }
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription ="" )
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
                        text = "Messages",
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
                    text = "RECENT",
                    fontSize = 13.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray,
                    letterSpacing = 3.2.sp
                )
            }
            Show(listContacts = listContacts)
            Box(modifier = modifier.height(25.dp))
            Box(
                modifier = modifier
                    .background(Color.White, shape = Shapes.large)
                    .fillMaxSize(),
                contentAlignment =  Alignment.TopStart
            ) {
//                if(isLoading){
//                    CircularProgressIndicator()
//                }
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 5.dp, vertical = 30.dp)
                        .fillMaxWidth()
                ) {
                    items(userLists.size){
                       index ->
                            val lt= if (users?.get(userLists[index].uid)?.lastMessage==null)"New" else users?.get(userLists[index].uid)?.lastMessage.toString()
                            val laT= if(users?.get(userLists[index].uid)?.lastTime==null) "" else users?.get(userLists[index].uid)?.lastTime.toString()
                            val unRe= if(users?.get(userLists[index].uid)?.unReadMessage==null) 0 else users?.get(userLists[index].uid)?.unReadMessage!!.toInt()
                            ContactCard(
                                user = userLists[index],
                                lastMessage = if(status=="Typing...") "Typing..." else lt,
                                lastChatTime = if(laT.isNotEmpty()) getHhMM(laT) else "",
                                numberOfMessage = unRe
                            ) {
                                chatViewModel.setRecName(userLists[index])
                                chatViewModel.getMessages(userLists[index].uid.toString())
                                chatViewModel.clearUnRead(userLists[index].uid.toString())
                                navController.navigate(Screen.Chat.route)
                            }

                    }
                }
            }
        }
    }
}
@Composable
fun Show(
    listContacts:List<ContactsList>
) {
    LazyRow(modifier = Modifier.wrapContentHeight(align = Alignment.Top)){
        items(items=listContacts){
                cont->
            println(cont.toString())
            if(cont.status) {
                StatusInfo(cont.image, cont.name, true)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun HomeScreenPreview() {
//    HomeScreen(modifier = Modifier, navController = rememberNavController())
}
