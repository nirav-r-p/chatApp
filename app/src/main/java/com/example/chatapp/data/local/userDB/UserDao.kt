package com.example.chatapp.data.local.userDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OfflineDao {
    @Query("Select loginUser from OfflineData")
    fun getLoginUser():String

    @Query("Select contactInfo from OfflineData")
    fun getContactInfo():String

    @Query("Select lastMessage from OfflineData")
    fun getMapLastMessage():String

    @Query("Update OfflineData set loginUser=:data")
    fun updateLogin(data:String)

    @Query("Update OfflineData set contactInfo=:data")
    fun updateContactInfo(data:String)

    @Query("Update OfflineData set lastMessage=:data")
    fun updateMapLastMessage(data:String)

    @Insert
    fun initialDB(data: OfflineData)

    @Query("Select * from OfflineData")
    fun isEmptyDB(): List<OfflineData>
}