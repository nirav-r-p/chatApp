package com.example.chatapp.component

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.example.chatapp.R

class ContactsList(
    val image:Int,
    val name:String,
    val status:Boolean
)

class GetInfo{
    fun getContact():List<ContactsList>{
        return listOf(
            ContactsList(
                image = R.drawable.img,
                name = "Berry",
                status = true
            ),
            ContactsList(
                image = R.drawable.img_1,
                name = "Perez",
                status = true
            ),
            ContactsList(
                image = R.drawable.img_2,
                name = "Alvin",
                status = true
            ),
            ContactsList(
                image = R.drawable.img_3,
                name = "Dan",
                status = true
            ),
            ContactsList(
                image = R.drawable.img_5,
                name = "Joy",
                status = false
            ),
            ContactsList(
                image = R.drawable.img_5,
                name = "Berry",
                status = true
            ),

        )
    }
}

class ChatItem(
    text:String,
    color: Color,
    boxShape: Shape,
    alignment: Alignment= Alignment.CenterStart
    ){
    val message:String=text
    val boxColor:Color=color
    val boxShapes=boxShape
    val boxAlignment=alignment
}

