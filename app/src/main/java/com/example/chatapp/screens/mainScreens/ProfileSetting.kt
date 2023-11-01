package com.example.chatapp.screens.mainScreens

import android.content.res.Resources.Theme
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.chatapp.R
import com.example.chatapp.component.UserInfoLabel
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetting(
    viewModel: UserViewModel,
    navController: NavController,
    onLogOut:()->Unit,
) {
    val user by viewModel.logInUser.observeAsState()
    val galleryLauncher= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(), onResult ={
        if (it != null) {
            viewModel.uploadImage(it)
        }
    } )
    val loadImage by viewModel.loadingImage.observeAsState(initial = false)
    Scaffold(
        topBar = {
            Box(modifier = Modifier.height(60.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }

                    Text(
                        text = "Profile Setting",
                        fontSize = 26.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 4.dp)
                    )
                }
            }

        }
    ) {
        paddingValues -> 
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
                    ,
                contentAlignment = Alignment.Center
            ) {
                if (loadImage){
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }else {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.size(200.dp)
                    ) {
                        Image(
                            painter = if (user?.pic?.isBlank() == true) painterResource(R.drawable.img_4) else rememberAsyncImagePainter(
                                model = user?.pic
                            ),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .clip(
                                    CircleShape
                                )
                                .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                                .size(200.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Blue)
                        ) {
                            IconButton(
                                onClick = { galleryLauncher.launch("image/*") },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PhotoCamera,
                                    contentDescription = ""
                                )
                            }
                        }

                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    UserInfoLabel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription ="User",
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        headingText = "Name",
                        text = user?.userName.toString()
                    )
                    UserInfoLabel(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription ="User",
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        headingText = "Tag Line",
                        text=user?.tag.toString()
                    )
                    UserInfoLabel(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription ="User",
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        headingText = "Email",
                        text=user?.email.toString()
                    )
                    UserInfoLabel(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription ="User",
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        headingText = "Phone",
                        text= user?.number.toString().ifBlank { "--" }
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 18.dp)
                        ) {
                            Text(
                                text = "Created at ",
                                fontFamily = poppinsFont,

                            )
                            Text(
                                text = user?.date.toString().split(" ")[0].ifBlank { "--" },
                                fontFamily = poppinsFont,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                        }
                    }
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f),
                        contentAlignment = Alignment.Center
                    ){
                        TextButton(onClick = {
                            viewModel.logoutUser()
                            onLogOut()
                        }) {
                            Text(
                                text = "~LogOut~"
                            )
                        }
                    }
                }
            }


        }
    }
}


@Preview
@Composable
fun ProfileSettingPreview() {
    ProfileSetting(viewModel = UserViewModel(), navController = rememberNavController(),{})
}