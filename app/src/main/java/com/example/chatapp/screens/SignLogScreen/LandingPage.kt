package com.example.chatapp.screens.SignLogScreen


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.ChatBoxShape
import com.example.chatapp.ui.theme.poppinsFont

@SuppressLint("SuspiciousIndentation")
@Composable
fun LandingPage(
    modifier: Modifier,
    navController: NavController
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.Black, shape = ChatBoxShape.large)) {
            Image(painter = painterResource(id = R.drawable.img_7), contentDescription ="" , contentScale = ContentScale.FillBounds, modifier = modifier.fillMaxSize())
        }
        Box (modifier = modifier
            .weight(1.1f)
            .background(Color.Black)
            .padding(36.dp)
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Stay connected with your friends and family",
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 36.sp,
                )
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription ="",
                        tint = Color.Green,
                        modifier = modifier.padding(2.dp)
                    )
                    Text(
                        text = "Secure, private messaging",
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = modifier.padding(2.dp)
                    )
                }
                Box(modifier = modifier
                    .weight(1f)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {
                    ElevatedButton(
                        onClick = {
                                    navController.navigate(Screen.SignInScreen.route)
                                  },
                        modifier = modifier
                        .fillMaxWidth()
                        .height(64.dp)
                    )
                    {
                        Text(
                            text = "Get Started",fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LandingPagePreview() {
//    LandingPage(modifier = Modifier)
//}