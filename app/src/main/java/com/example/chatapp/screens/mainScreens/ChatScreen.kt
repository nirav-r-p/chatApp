package com.example.chatapp.screens.mainScreens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.chatapp.R
import com.example.chatapp.component.CardHeader
import com.example.chatapp.ui.theme.ChatBoxShape
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.use_case.MessageModel
import com.example.chatapp.use_case.viewModels.ChatViewModel
import com.google.android.play.integrity.internal.f
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    modifier: Modifier,
    viewModel: ChatViewModel
) {
    val messageList by viewModel.messages.observeAsState(initial = emptyList())
    val status by viewModel.status.observeAsState(initial = "Online")
    val user by viewModel.recName.observeAsState()
    val senderId= FirebaseAuth.getInstance().currentUser?.uid
    val receiverRoom=user?.uid+senderId
    val coroutineScope= rememberCoroutineScope()
    if(user!=null){
         val myDBRef= FirebaseDatabase.getInstance().reference
        val senderRoom=senderId+user?.uid
        myDBRef.child("user").child(senderId!!).child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                var messageLists= mutableListOf<MessageModel>()
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageLists.clear()
                    var count = 0
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(MessageModel::class.java)
                        messageLists.add(message!!)
                        count++
                        Log.d("Messages", "$count")
                    }
                    viewModel.setMessage(messageLists)
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        myDBRef.child("user").child(user?.uid!!).child("chats").child(receiverRoom!!).child("Status").addValueEventListener(
            object : ValueEventListener  {
                override fun onDataChange(snapshot: DataSnapshot) {
                        when (snapshot.value){
                            "Typing"->viewModel.getStatus("Typing...")
                            "Online"->viewModel.getStatus("Online")
                            "Offline"->viewModel.getStatus("")
                        }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
    val lazyScroll= rememberLazyListState()
    var messageText by remember {
        mutableStateOf("")
    }
      Scaffold(
          topBar = {
              CardHeader(
                  userName = user?.userName.toString(),
                  userProfilePic = R.drawable.img_2,
                  action = status.toString(),
                  verticalPadding = 20.dp,
                  horizontalPadding = 8.dp
              )
          }
      ) { padding->
          Column(
              modifier = modifier
                  .fillMaxSize()
                  .padding(padding)
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
                          verticalArrangement = Arrangement.Top,
                          state = lazyScroll
                      ) {
                          items(messageList.size) { index ->
                              SendBox(modifier, messageList[index])
                          }
                      }
                  }

              }
              Box(
                  modifier = modifier
                      .fillMaxWidth()
                      .background(color = Color.White)
                      .padding(2.dp),
                  contentAlignment = Alignment.BottomCenter
              ) {
                  OutlinedTextField(
                      value = messageText, onValueChange = {
                          viewModel.setStatus(recId = user?.uid.toString(), "Typing")
                          messageText = it
                      },
                      shape = Shapes.large,
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(10.dp),
                      trailingIcon = {
                          IconButton(
                              onClick = {
                                  coroutineScope.launch {
                                      if (messageList.isNotEmpty()) {
                                          lazyScroll.animateScrollToItem(messageList.size - 1)
                                      }
                                  }
                                  if (user?.uid != null) {
                                      viewModel.setStatus(recId = user?.uid.toString(), "Online")
                                      viewModel.addMessage(
                                          recId = user?.uid.toString(),
                                          message = messageText
                                      )
                                      messageText = ""
                                  }

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
      }
    }


@Composable
fun SendBox(
    modifier: Modifier,
    messageModel: MessageModel
) {
    var pos=messageModel.senderId!=FirebaseAuth.getInstance().currentUser?.uid
    Row(
        modifier = modifier.fillMaxWidth(),
        ){
        if (!pos){
            Box(modifier = Modifier
                .size(8.dp)
                .background(color = Color.Green, Shapes.extraSmall))
        }
        SendText(sendText = messageModel,pos)
        if(pos){
            Box(modifier = Modifier
                .size(8.dp)
                .background(color = Color.Green, Shapes.extraSmall))
        }
    }
   Box(modifier = Modifier.height(12.dp))
}
@Composable
fun SendText(sendText:MessageModel,bool:Boolean) {
    val textAlign=if(bool){
       Alignment.End
    }else{
        Alignment.Start
    }

    Box (
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
       Column(
           horizontalAlignment = textAlign,
           modifier = Modifier.fillMaxWidth()
       ) {
           Text(
               text = sendText.message.toString(),
               fontSize = 15.sp,
               fontFamily = poppinsFont,
               fontWeight = FontWeight.Normal,
               color = Color.Black
           )
           Text(
               text = sendText.sendTime.toString(),
               fontSize = 10.sp,
               fontFamily = poppinsFont,
               fontWeight = FontWeight.Normal,
               color = Color.Black
           )
       }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun ChatScreenPreview() {
    ChatScreen(modifier = Modifier, viewModel = ChatViewModel())
}