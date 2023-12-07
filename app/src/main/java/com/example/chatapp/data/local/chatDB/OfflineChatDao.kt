package com.example.chatapp.data.local.chatDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.chatapp.databaseSchema.UserMessage

@Dao
interface OfflineChatDao {
   @Insert
   fun createChat(data:OfflineChatEntity)

   @Query("Update OfflineChatEntity set pendingMessage=:data") // call in Home Screen
   fun clearPendingMessage(data: String="")

   @Query("Select pendingMessage from OfflineChatEntity where userId==:recId")
   suspend fun getPendingMessage(recId: String):String

   @Query("Update OfflineChatEntity set allMessage=:data where userId==:recId")
   fun updateAllMessage(recId:String,data: String)

   @Query("Select * from OfflineChatEntity") //call in HomeScreen
   suspend fun getAllPaddingMessage():List<OfflineChatEntity>

   @Query("Update OfflineChatEntity set pendingMessage=:message where userId==:id")
   fun updatePendingMessage(id: String,message: String)
   @Query("Select Count(*) from offlinechatentity where userId==:id")
   fun idAvailable(id:String):Int

   @Query("Select allMessage from OfflineChatEntity where userId==:recId")
   suspend fun getAllMessage(recId: String):String

}