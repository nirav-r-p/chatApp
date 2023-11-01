package com.example.chatapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.chatapp.R
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.databaseSchema.UserInfo
import com.google.android.play.integrity.internal.l

@Composable
fun ContactCard(
    user: UserInfo,
    lastMessage:String,
    lastChatTime:String,
    numberOfMessage:Int=0,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                contentAlignment =  Alignment.Center
            ) {
                Image(
                    painter = if(user.pic=="") painterResource(R.drawable.img_4) else rememberAsyncImagePainter(
                        model = user.pic
                    ),
                    contentDescription = "" , contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                        .size(51.dp)
                )
                if(numberOfMessage>0){
                    Box (
                        modifier = Modifier
                            .height(51.dp)
                            .width(51.dp),
                        contentAlignment = Alignment.TopEnd){
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(15.dp)
                                .width(15.dp)
                                .background(color = Color.Yellow),
                            contentAlignment = Alignment.Center

                        ){

                            Text(
                                text = numberOfMessage.toString(),
                                fontSize = 9.sp,
                                color = Color.Black,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2.0f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.userName.toString(),
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.SemiBold,
                    fontSize=15.sp,
                    color= Color.Black
                )
                Text(
                    text =lastMessage,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    fontSize=14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(top = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = lastChatTime,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    fontSize=12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(1.5.dp))
                Text(
                    text = user.tag.toString(),
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    fontSize=10.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}

