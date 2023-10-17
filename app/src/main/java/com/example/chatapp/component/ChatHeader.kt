package com.example.chatapp.component



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.ui.theme.poppinsFont

@Composable
fun CardHeader(
    userName:String="Berry Ab",
    userProfilePic:Int= R.drawable.img_4,
    action:String,
    horizontalPadding: Dp =6.dp,
    verticalPadding: Dp =6.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .clickable { }
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding, horizontal =horizontalPadding )
                .clickable { }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    contentAlignment =  Alignment.Center
                ) {
                    Image(
                        painter = painterResource(userProfilePic),
                        contentDescription = "" , contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                            .size(51.dp)
                    )


                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2.0f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = userName,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.SemiBold,
                        fontSize=15.sp,
                        color= Color.White
                    )
                    Text(
                        text = action,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        fontSize=14.sp,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription ="More", modifier = Modifier.size(45.dp), tint = Color.White )
                }
            }
        }
    }

}

//@Preview(showBackground = true, backgroundColor = 0)
//@Composable
//fun CardHeaderPreview() {
//    CardHeader(onLineStatus = true)
//}