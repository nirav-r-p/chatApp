package com.example.chatapp.screens.mainScreens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.UiMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteUser(
  userViewModel: UserViewModel,
  navController: NavController
) {
    var userName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
   Scaffold {
       padding->
       Column(
           modifier = Modifier
               .padding(padding)
               .fillMaxSize()
       ) {
           Text(
               text = "Enter Contact",
               fontSize = MaterialTheme.typography.headlineLarge.fontSize,
               fontFamily = poppinsFont,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.padding(15.dp)
           )
          OutlinedTextField(
              value = userName,
              onValueChange = {
              userName=it
              },
              label = {
                  Text(
                      text = "First Name", fontSize = 15.sp,
                      fontFamily = poppinsFont,
                      fontWeight = FontWeight.Normal
                  )
              }, textStyle = TextStyle(
                  color = Color.Black
              ),
              maxLines = 2,
              leadingIcon = {
                  Icon(imageVector = Icons.Default.Person, contentDescription = "")
              },
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 8.dp, horizontal = 12.dp)
          )
           OutlinedTextField(
               value = lastName,
               onValueChange = {
                 lastName=it
               },
               label = {
                   Text(
                       text = "Last Name ", fontSize = 15.sp,
                       fontFamily = poppinsFont,
                       fontWeight = FontWeight.Normal
                   )
               }, textStyle = TextStyle(
                   color = Color.Black
               ),
               leadingIcon = {
                   Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
               },
               maxLines = 2,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 8.dp, horizontal = 12.dp)
           )
           OutlinedTextField(
               value = email,
               onValueChange = {
                   email=it
               },
               label = {
                   Text(
                       text = "Enter Email", fontSize = 15.sp,
                       fontFamily = poppinsFont,
                       fontWeight = FontWeight.Normal
                   )
               }, textStyle = TextStyle(
                   color = Color.Black
               ),
               leadingIcon = {
                   Icon(imageVector = Icons.Default.Email, contentDescription = "")
               },
               maxLines = 2,
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 8.dp, horizontal = 12.dp)
           )
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(3f)
                   .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
               contentAlignment = Alignment.BottomCenter
           ) {
               Button(
                   onClick = {
                       userViewModel.addContact("$userName $lastName", email = email)
                       navController.popBackStack()
                             },
                   modifier = Modifier.fillMaxWidth()
               ) {
                   Text("Save")
               }
           }
       }
   }
}

@Preview()
@Composable
fun InviteUserPreview() {
    InviteUser(userViewModel = UserViewModel(), navController = rememberNavController())
}