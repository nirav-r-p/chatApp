package com.example.chatapp.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.ui.theme.poppinsFont

@Composable
fun UserInfoLabel(
    modifier: Modifier,
    leadingIcon:@Composable() (()->Unit),
    headingText:String,
    text:String
) {
    var text by remember {
        mutableStateOf(text)
    }
    var isEdited by remember {
        mutableStateOf(false)
    }
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            leadingIcon()
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f)

        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = headingText,
                    fontFamily = poppinsFont,
                )
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = poppinsFont,
                    fontSize = 14.sp
                )
                if (headingText=="Name") {
                    Text(
                        text = "This Username visible to other.",
                        fontFamily = poppinsFont, fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if(headingText!="Email") {
                IconButton(onClick = { isEdited=true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edited",
                        modifier = Modifier.size(30.dp)
                    )
                }

            }
        }

    }
}