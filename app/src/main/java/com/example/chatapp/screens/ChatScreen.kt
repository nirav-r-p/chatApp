package com.example.chatapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.component.CardHeader
import com.example.chatapp.component.ChatItem
import com.example.chatapp.ui.theme.ChatBoxShape
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.ui.theme.receiveBoxShape
import com.example.chatapp.ui.theme.receiveColor
import com.example.chatapp.ui.theme.sendBoxShape
import com.example.chatapp.ui.theme.sendColor


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier,
    lastMassage:String="Hi Nirav, I am Looking For Android Dev?",
    userImage:Int= R.drawable.img_5,
    name:String="User"
) {
    var messageText by remember {
        mutableStateOf("")
    }
    var listOfMessage by remember {
        mutableStateOf(listOf(ChatItem(text = lastMassage, color = receiveColor, boxShape = receiveBoxShape.small)))
    }

    Scaffold(
        topBar = {
            CardHeader(
                userName = name,
                userProfilePic = userImage,
                onLineStatus = true,
                verticalPadding = 20.dp,
                horizontalPadding = 8.dp
            )
        },
        bottomBar = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(10.dp)
            ) {
                OutlinedTextField(
                    value = messageText, onValueChange = { messageText = it },
                    shape = Shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                listOfMessage += ChatItem(
                                    text = messageText,
                                    color = sendColor,
                                    boxShape = sendBoxShape.small,
                                    alignment = Alignment.CenterEnd
                                )
                                messageText = ""
                            },
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                        }
                    },
                    label = {
                        Text(
                            text = "Type .. ", fontSize = 15.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Normal
                        )
                    }, textStyle = TextStyle(
                        color = Color.Black
                    ),
                    maxLines = 2
                )
            }
        }
    ) {
        padding->
        Column(
            modifier = modifier.fillMaxSize().padding(padding)
        ) {


            Box(
                modifier = modifier
                    .background(Color.White, shape = ChatBoxShape.large)
                    .fillMaxWidth()
                    .weight(3.0f),

                ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxWidth()
                            .weight(3.0f)
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,

                        ) {
                        items(listOfMessage) { message ->
                            SendBox(modifier, message)
                        }
                    }
                }

            }
        }
    }
}
@Composable
fun SendBox(
    modifier: Modifier,
    chatText:ChatItem,
) {
    Box (
        modifier = modifier.fillMaxWidth(),
        contentAlignment = chatText.boxAlignment){
        SendText(sendText = chatText.message, sendColor = chatText.boxColor, chatText.boxShapes)
    }
   Box(modifier = Modifier.height(12.dp))
}
@Composable
fun SendText(sendText:String,sendColor:Color= receiveColor,sendBox: Shape) {
    Box (
        modifier = Modifier
            .background(sendColor, shape = sendBox)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = sendText,
            fontSize = 15.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }

}
@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun ChatScreenPreview() {
    ChatScreen(modifier = Modifier)
}