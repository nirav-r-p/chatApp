package com.example.chatapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes=Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(40.dp)
)
var contectContenerShapes=Shapes(
    large = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
)
val sendBoxShape=Shapes(
    small= RoundedCornerShape(20.dp,0.dp,20.dp,20.dp)
)
val receiveBoxShape=Shapes(
    small= RoundedCornerShape(0.dp,20.dp,20.dp,20.dp)
)
val ChatBoxShape=Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(topEnd = 40.dp, topStart = 40.dp)
)