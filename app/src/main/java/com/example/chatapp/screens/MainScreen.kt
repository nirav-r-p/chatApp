package com.example.chatapp.screens

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.LandingActivity
import com.example.chatapp.MainActivity
import com.example.chatapp.component.ContactCard
import com.example.chatapp.component.ContactsList
import com.example.chatapp.component.GetInfo
import com.example.chatapp.component.StatusInfo
import com.example.chatapp.R
import com.example.chatapp.database.UserAuth
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier,navController: NavController) {
    val user=UserAuth()
    val listContacts= GetInfo().getContact()

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    user.logoutUser()

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
                    IconButton(onClick = { /*TODO*/ }) {
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
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 5.dp, vertical = 30.dp)
                        .fillMaxWidth()
                ) {
                    item {
                        ContactCard(
                            numberOfMessage = 2,
                            userProfilePic = R.drawable.img_2,
                            userName = "Alvin",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Alvin",
                                        id = R.drawable.img_2
                                    )
                                )
                            })
                        ContactCard(
                            userProfilePic = R.drawable.img_1,
                            numberOfMessage = 1,
                            userName = "Perez",
                            lastChatTime = "7.45 am",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Perez",
                                        id = R.drawable.img_1
                                    )
                                )
                            })
                        ContactCard(
                            userProfilePic = R.drawable.img_3,
                            numberOfMessage = 3,
                            userName = "Dan",
                            lastChatTime = "5.00 pm",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Dan",
                                        id = R.drawable.img_3
                                    )
                                )
                            })
                        ContactCard(
                            userName = "Alex",
                            lastChatTime = "27/3/23",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Alex",
                                        id = R.drawable.img_5
                                    )
                                )
                            })
                        ContactCard(
                            userProfilePic = R.drawable.img,
                            numberOfMessage = 2,
                            lastChatTime = "01/01/23",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Berry Ab",
                                        id = R.drawable.img
                                    )
                                )
                            })
                        ContactCard(
                            userName = "Mex",
                            lastChatTime = "01/10/22",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Max",
                                        id = R.drawable.img_5
                                    )
                                )
                            })
                        ContactCard(
                            userName = "Luffy",
                            lastChatTime = "02/08/2022",
                            onClick = {
                                navController.navigate(
                                    route = Screen.Chat.getUserImage(
                                        name = "Luffy",
                                        id = R.drawable.img_5
                                    )
                                )
                            })
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
    HomeScreen(modifier = Modifier, navController = rememberNavController())
}