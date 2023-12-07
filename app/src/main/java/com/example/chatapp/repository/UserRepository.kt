package com.example.chatapp.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.chatapp.data.local.chatDB.OfflineChatDB
import com.example.chatapp.data.local.chatDB.OfflineChatEntity
import com.example.chatapp.data.local.userDB.OfflineData
import com.example.chatapp.data.local.userDB.OfflineDataBase
import com.example.chatapp.data.network.ContactLists
import com.example.chatapp.data.network.NetworkDB
import com.example.chatapp.databaseSchema.MessageModel
import com.example.chatapp.databaseSchema.UserInfo
import com.example.chatapp.databaseSchema.UserMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class UserRepository(
    private val networkDB: NetworkDB,
    private val offlineDB: OfflineDataBase,
    private val offlineChatDB: OfflineChatDB
){
    private var net:Boolean =true
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun set(value:Boolean){
        net=value
        if (net){
            updateAllMessage()
        }
        offlineChatDB.offlineChatDao
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateAllMessage(){
        withContext(Dispatchers.Default){
            val pendingMessage=offlineChatDB.offlineChatDao.getAllPaddingMessage()
            if (pendingMessage!=null) {
                Log.d("update user repo chat", "updateAllMessage: $pendingMessage")
                val mapValue = getMapMessage(pendingMessage)
                if (mapValue.values.isNotEmpty()) {
                    networkDB.updatePendingMessage(mapValue).collect {
                        if (it) {
                            offlineChatDB.offlineChatDao.clearPendingMessage()
                        }
                    }
                    Log.d("Update message", "updateAllMessage: Call for update $mapValue")
                }
            }

        }

    }
    suspend fun getContactId():Flow<List<ContactLists>>{
        if (net) {
            return networkDB.getContacts()
        }
        return flowOf(emptyList())
    }
    suspend fun getContactList(contactLists: List<ContactLists>): Flow<List<UserInfo>> {
        return withContext(Dispatchers.IO) {
            if (checkIsEmpty()) {
                offlineDB.offlineDao.initialDB(
                    OfflineData(
                        loginUser = "",
                        contactInfo = "",
                        message = "",
                        lastMessage = ""
                    )
                )
            }
            if (net) {
                Log.d("Repository", "getContact List: Net call")
                val result = networkDB.getContactInfo(contactLists)
                result.collect {
                    offlineDB.offlineDao.updateContactInfo(listObjectToString(it))
                }
                return@withContext result
            }
            val jsonString = offlineDB.offlineDao.getContactInfo()
            val list = stringToListObject(jsonString)
            return@withContext flowOf(list)
        }
    }
   suspend fun updateMassage():Flow<Map<String,UserMessage>>{
       return withContext(Dispatchers.IO){
           if (net) {
               Log.d("Repository", "getMap: Net call")
               val result=networkDB.getContactChat()
               result.collect{
                   offlineDB.offlineDao.updateMapLastMessage(mapToString(it))
               }
           }
           val jsonString=offlineDB.offlineDao.getMapLastMessage()
           val mapValue= stringToMap(jsonString)
           return@withContext flowOf(mapValue)
       }


   }
    suspend fun getLoginUserInfo():Flow<UserInfo>{
       return withContext(Dispatchers.IO) {
            val jsonString = offlineDB.offlineDao.getLoginUser()
            if (net) {
                Log.d("Repository", "getLoginUserInfo: Net call")
                val login = networkDB.getLoginUser()
                login.collect {
                    offlineDB.offlineDao.updateLogin(it.toJsonData())
                }
                return@withContext login
            }
            val loginUser = Gson().fromJson(jsonString, UserInfo::class.java)
            return@withContext flowOf(loginUser)
        }
    }

     private suspend fun checkIsEmpty():Boolean{
        return withContext(Dispatchers.IO){ offlineDB.offlineDao.isEmptyDB().isEmpty()}
     }
}
fun listObjectToString(list: List<UserInfo>): String {
    val gson = Gson()
    return gson.toJson(list)
}

fun stringToListObject(jsonString: String): List<UserInfo> {
    val gson = Gson()
    val listType = object : TypeToken<List<UserInfo>>() {}.type
    return gson.fromJson(jsonString, listType)
}

fun stringToMap(jsonString: String):Map<String,UserMessage>{
    val gson=Gson()
    val mapValue=object :TypeToken<Map<String,UserMessage>>() {}.type
    return gson.fromJson(jsonString,mapValue)
}
fun mapToString(map: Map<String,UserMessage>): String {
    val gson = Gson()
    return gson.toJson(map)
}
//   fun addContact(user: String, email: String):Resource<String>{
//     return networkDB.addContact(user,email)
//   }

fun getMapMessage(data:List<OfflineChatEntity>):Map<String,List<MessageModel>>{
    val value= mutableMapOf<String,List<MessageModel>>()
    data.forEach {
        Log.d("OfflineData", "getMapMessage: $it")
        if (it.pendingMessage.isNotBlank()) {
            val list = stringToList(it.pendingMessage)
            val id = it.userId
            value[id] = list
        }
    }
    return value
}