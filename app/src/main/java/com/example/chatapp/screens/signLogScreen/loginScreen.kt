package com.example.chatapp.screens.signLogScreen



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.navigationComponent.Screen
import com.example.chatapp.ui.theme.ChatBoxShape
import com.example.chatapp.ui.theme.Shapes
import com.example.chatapp.ui.theme.poppinsFont
import com.example.chatapp.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: LoginViewModel,
    onNavigate:()->Unit
) {
    val keyboardController =LocalSoftwareKeyboardController.current
    val message by viewModel.message.observeAsState(initial = "")
    val isVerify by viewModel.verify.observeAsState(initial = false)
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    viewModel.loadingState.observe(LocalLifecycleOwner.current) { loadingState ->
        isLoading = when (loadingState) {
            LoginViewModel.UiLoadingState.IsLoading -> {
                true
            }
            LoginViewModel.UiLoadingState.IsNotLoading -> {
                false
            }
        }
    }
    if (isVerify){
        onNavigate()
    }
    Column(modifier = modifier.fillMaxSize())
    {
        Box(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .background(Color.Black, shape = ChatBoxShape.large)) {
            Image(painter = painterResource(id = R.drawable.img_7), contentDescription ="" , contentScale = ContentScale.FillBounds, modifier = modifier
                .fillMaxSize()
                .zIndex(5f))
        }
        Box (modifier = modifier
            .weight(1.4f)
            .background(Color.Black)
            .padding(34.dp)
        ){
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tell us about your Self",
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 36.sp,
                    lineHeight = 34.sp
                )
              
                Box(modifier = modifier
                    .weight(1f)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier= Modifier
                            .safeContentPadding()
                            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(text = "User Email") },
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(horizontal = 2.dp)
                                .background(Color.White, shape = RoundedCornerShape(30.dp)),
                            singleLine = true,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = Shapes.medium
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = "Password") },
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .padding(horizontal = 2.dp)
                                .background(Color.White, shape = RoundedCornerShape(30.dp)),
                            singleLine = true,
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = Shapes.medium,
                            visualTransformation = PasswordVisualTransformation()
                        )
                        ElevatedButton(
                            onClick = {
                                viewModel.loginUser(email, password) { isSuccess, errorMessage ->
                                    if (isSuccess) {
                                        navController.clearBackStack(Screen.LandingPage.route)
                                        navController.clearBackStack(Screen.SignInScreen.route)
                                        navController.clearBackStack(Screen.LoginScreen.route)

                                    } else {
                                        // Show error message to the user
                                        print(errorMessage.toString())
                                    }
                                }
                            },
                            modifier = modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        )
                        {
                            if (isLoading){
                                CircularProgressIndicator()
                            }else {
                                Text(
                                    text =if (message=="Please Verify Your Email Id") message.toString() else  "Let’s Chat", fontFamily = poppinsFont,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Forgot Password??")
                    }
                }
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen(modifier = Modifier)
//}