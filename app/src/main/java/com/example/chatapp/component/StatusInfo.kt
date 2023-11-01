package com.example.chatapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.chatapp.R
import com.example.chatapp.ui.theme.poppinsFont

@Composable
fun StatusInfo(
    image:String,
    name:String="Berry",
    currentStatus:Boolean=true
) {
    Box (
        modifier = Modifier
        .height(98.dp)
        .width(85.dp).
        padding(top = 5.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .height(98.dp)
                .width(65.dp)
                .padding(start = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter =if(image.isBlank()) painterResource(R.drawable.img_5) else rememberAsyncImagePainter(
                            model = image
                    ),
                    contentDescription = "" , contentScale = ContentScale.FillBounds,
                    modifier = Modifier.clip(
                        CircleShape
                    ).size(65.dp)
                )
                if(currentStatus){
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(15.dp)
                            .width(15.dp)
                            .background(color = Color.Green)

                    )
                }
            }
            Text(
                text = name,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal
            )
        }
    }

}


@Composable
@Preview(showBackground = true)
fun StatusInfoPreview() {
    StatusInfo(image = "")
}