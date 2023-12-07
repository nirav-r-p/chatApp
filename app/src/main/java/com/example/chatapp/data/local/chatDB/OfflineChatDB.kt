package com.example.chatapp.data.local.chatDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [OfflineChatEntity::class], version = 1)
abstract class OfflineChatDB:RoomDatabase() {
    abstract val offlineChatDao:OfflineChatDao

    companion object {
        @Volatile
        private var INSTANCE: OfflineChatDB? = null

        fun getInstance(context: Context): OfflineChatDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OfflineChatDB::class.java,
                    "your_database_name"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}