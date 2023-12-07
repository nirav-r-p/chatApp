package com.example.chatapp.data.local.userDB

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [OfflineData::class], version = 1)
abstract class OfflineDataBase:RoomDatabase() {
    abstract val offlineDao: OfflineDao
}