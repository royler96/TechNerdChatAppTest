package com.basemp.chatapptechnerd.preferences

import android.content.Context
import android.content.SharedPreferences
import com.basemp.chatapptechnerd.entities.UserEntity
import com.basemp.chatapptechnerd.helpers.GsonHelper
import com.google.firebase.auth.FirebaseUser

class MDefaultSharedPref : IMSharedPreferences {
    private lateinit var context: Context
    private var appId = "chatapptechnerd_preferences"

    private object Holder { val INSTANCE = MDefaultSharedPref() }

    companion object {
        const val SESSION = ".session.tech.user"
        const val TOKEN_FCM = ".session.tech.token.fcm"
        val instance: MDefaultSharedPref by lazy { Holder.INSTANCE }
    }

    override fun build(contxt: Context){
        context= contxt
    }

    override fun saveSession(user: UserEntity) {
        getEditor(context).putString(keySession(), GsonHelper.generateJSONObject(user).toString()).apply()
    }

    override fun isSession(): Boolean {
        val jsonSession=getSharedPreferences(context).getString(keySession(),null)
        return (jsonSession!=null)
    }

    override fun session(): UserEntity? {
        val jsonSession:String=getSharedPreferences(context).getString(keySession(),"")?:""
        val userSession:UserEntity= GsonHelper.convertJsonToClass(jsonSession, UserEntity::class.java)
        return userSession
    }

    override fun clearSession() {
        getEditor(context)
            .clear()
            .apply()
    }

    override fun saveToken(token: String) {
        TODO("Not yet implemented")
    }

    override fun getToken(): String {
        TODO("Not yet implemented")
    }

    override fun saveTokenFCM(tokenFCM: String) {
        getEditor(context).putString(keyTokenFCM(),tokenFCM).apply()
    }

    override fun getTokenFCM(): String {
        return getSharedPreferences(context).getString(keyTokenFCM(),"")?:""
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(appId, Context.MODE_PRIVATE)
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        val preferences = getSharedPreferences(context)
        return preferences.edit()
    }

    private fun keySession():String{
        return appId+ SESSION
    }

    private fun keyTokenFCM():String{
        return appId+ TOKEN_FCM
    }
}