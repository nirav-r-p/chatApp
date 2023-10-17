package com.example.chatapp.screens.mainScreens

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.chatapp.use_case.viewModels.ChatViewModel
import com.example.chatapp.use_case.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel
) {
    val userLists by userViewModel.userList.observeAsState(initial = emptyList())
    val users by userViewModel.users.observeAsState(initial = emptyList())
    val user=UserAuth()
    val listContacts= GetInfo().getContact()

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
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 5.dp, vertical = 30.dp)
                        .fillMaxWidth()
                ) {
                    items(userLists.size){
                       index ->
                        ContactCard(
                            user = userLists[index]
                        ) {
                            chatViewModel.setRecName(userLists[index])
                            chatViewModel.getMessages(userLists[index].uid.toString())
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
