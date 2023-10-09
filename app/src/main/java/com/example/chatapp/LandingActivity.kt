package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.example.chatapp.database.UserAuth
import com.example.chatapp.screens.LandingPage

class LandingActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LandingPage(modifier = Modifier){
                val i=Intent(this,SigInActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val auth=UserAuth()
        if (auth.isLoggedIn()){
             val nav=Intent(this,MainActivity::class.java)
            this.startActivity(nav)
            finish()
        }
    }

}