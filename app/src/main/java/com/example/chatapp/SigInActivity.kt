package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.example.chatapp.database.UserAuth
import com.example.chatapp.screens.SignInScreen

class SigInActivity:ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SignInScreen(modifier = Modifier, context = this){
                val nav= Intent(this, MainActivity::class.java)
                this.startActivity(nav)
                finish()
            }
        }
    }
}