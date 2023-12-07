package com.example.chatapp.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.chatapp.data.local.chatDB.OfflineChatDB
import com.example.chatapp.data.local.chatDB.OfflineChatEntity
import com.example.chatapp.data.local.userDB.OfflineDataBase
import com.example.chatapp.data.network.FirebaseChatOperation
import com.example.chatapp.databaseSchema.MessageModel
import com.example.chatapp.databaseSchema.UserMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class ChatRepository(
    private val networkDB: FirebaseChatOperation,
    private val offlineChatDB: OfflineChatDB,
    private val offlineDB: OfflineDataBase,
)  {
    private var isNetAvailable=true

    fun setNetAvailable(value:Boolean){
        isNetAvailable=value
    }

    suspend fun getMessage(resId:String):Flow<List<MessageModel>>{
        return withContext(Dispatchers.IO){
             if(isNetAvailable) {
                 val result=  networkDB.getMessage(resId)
                 result.collect{
                     val jsonString=Gson().toJson(it)
                     val isAvailableId=offlineChatDB.offlineChatDao.idAvailable(resId)
                     Log.d("id available", "getMessage: $isAvailableId")
                     if (isAvailableId==0){
                         withContext(Dispatchers.Default) {
                             offlineChatDB.offlineChatDao.createChat(
                                 OfflineChatEntity(
                                     resId,
                                     "",
                                     jsonString
                                 )
                             )
                             Log.d("Local chat DB", "getMessage: Created local DB   $jsonString")
                         }
                     }
                     offlineChatDB.offlineChatDao.updateAllMessage(resId,jsonString)
                     Log.d("Local chat DB", "getMessage: insert local DB $jsonString")
                 }
                 return@withContext result
             }else{
                 val jsonString=offlineChatDB.offlineChatDao.getAllMessage(resId)
                 val jsonStringPending=offlineChatDB.offlineChatDao.getPendingMessage(resId)
                 Log.d("get offline chat", "getMessage: $resId $jsonString ----pending--- $jsonStringPending")
                 val list= stringToList(jsonString)
                 val list2= stringToList(jsonStringPending)
                 val mList: MutableList<MessageModel> =
                     list.toMutableList().plus(list2.toMutableList()).toMutableList()
                return@withContext flowOf(mList)
             }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addMessage(resId: String, message: String, time:String){
        return withContext(Dispatchers.IO){
            if (isNetAvailable){
                networkDB.addMessage(resId,message,time)
            }else{
                val isAvailableId=offlineChatDB.offlineChatDao.idAvailable(resId)
                if (isAvailableId==0){
                    val list= mutableListOf<MessageModel>()
                    list.add(MessageModel(message,resId,time))
                    val jsonString=Gson().toJson(list)
                    withContext(Dispatchers.Default) {
                        offlineChatDB.offlineChatDao.createChat(
                            OfflineChatEntity(
                                resId,
                                jsonString,
                                ""
                            )
                        )
                    }
                }else{
                    val jsonString=offlineChatDB.offlineChatDao.getPendingMessage(resId)
                    val list= stringToList(jsonString)
                    val mList: MutableList<MessageModel> = list.toMutableList()
                    mList.add(MessageModel(message,resId,time))
                    val jsonString2=Gson().toJson(mList)
                    offlineChatDB.offlineChatDao.updatePendingMessage(resId,jsonString2)

                    val mapString=offlineDB.offlineDao.getMapLastMessage()
                    val mapValue= stringToMap(mapString)
                    val newMap=mapValue.toMutableMap()
                    newMap[resId]=UserMessage(message,time,0)

                    var updatedMap=mapString
                    if (newMap.isNotEmpty()) {
                         updatedMap = Gson().toJson(newMap)
                    }
                    offlineDB.offlineDao.updateMapLastMessage(updatedMap)
                    Log.d("Add to LocalDB", "addMessage: Add to local")
                }
            }
        }
    }

    fun clearUnReadCount(resId: String){
        networkDB.clearUnReadCount(resId)
    }

}

fun stringToList(jsonString: String):List<MessageModel>{
    val gson= Gson()
    val mapValue=object : TypeToken<List<MessageModel>>() {}.type
    Log.d("List of Message offline", "stringToList: $jsonString")
    if (jsonString.isNotBlank()) {
        return gson.fromJson(jsonString,mapValue)
    }
    return emptyList()
}