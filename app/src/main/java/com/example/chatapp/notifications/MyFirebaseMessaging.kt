package com.example.chatapp.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.chatapp.ChatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessaging:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val sended=message.data["sended"]
        val user=message.data["user"]
        val  sharedPref=getSharedPreferences("PREFS", Context.MODE_PRIVATE )
        val currentOnlineUser=sharedPref.getString("currentUser","none")
        val firebaseUser=Firebase.auth.currentUser
        if (firebaseUser!=null && sended==firebaseUser.uid){
            if (currentOnlineUser!=null)
            {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    sendOreoNotification(message)
                }else{
                     sendNotification(message)
                }
            }
        }
    }

    private fun sendNotification(message: RemoteMessage) {
        val user=message.data["user"]
        val icon=message.data["icon"]
        val title=message.data["title"]
        val body=message.data["body"]

        val notification= message.notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent=Intent(this,ChatActivity::class.java)
        val bundle=Bundle()
        bundle.putString("UserId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,j,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder:NotificationCompat.Builder= NotificationCompat.Builder(this)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(icon!!.toInt())
            .setSound(defaultSound)
            .setAutoCancel(true)
        val  noti=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var i=0
        if(j>0){
            i=j
        }
       noti.notify(i,builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendOreoNotification(message: RemoteMessage) {
        val user=message.data["user"]
        val icon=message.data["icon"]
        val title=message.data["title"]
        val body=message.data["body"]

        val notification= message.notification
        val j=user!!.replace("[\\D]".toRegex(),"").toInt()
        val intent=Intent(this,ChatActivity::class.java)
        val bundle=Bundle()
        bundle.putString("UserId",user)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,j,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val defaultSound=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val oreoNotification = OreoNotification(this)

        val bundler:Notification.Builder =oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon)

        var i=0
        if(j>0){
            i=j
        }
        oreoNotification.getManger!!.notify(i,bundler.build())
    }
}