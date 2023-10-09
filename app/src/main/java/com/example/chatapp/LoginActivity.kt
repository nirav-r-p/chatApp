package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.example.chatapp.screens.LoginScreen

class LoginActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { 
            LoginScreen(
                modifier = Modifier,
                onClick = {
                    val nav = Intent(this, MainActivity::class.java)
                    this.startActivity(nav)
                    finish()

                }
            )
        }
    }
}