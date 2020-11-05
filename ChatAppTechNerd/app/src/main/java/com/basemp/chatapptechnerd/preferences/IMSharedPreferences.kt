package com.basemp.chatapptechnerd.preferences

import android.content.Context
import com.basemp.chatapptechnerd.entities.UserEntity
import com.google.firebase.auth.FirebaseUser

interface IMSharedPreferences {
    fun build(context: Context)
    fun saveSession(user: UserEntity)
    fun isSession():Boolean
    fun session():UserEntity?
    fun clearSession()
    fun saveToken(token: String)
    fun getToken():String
    fun saveTokenFCM(tokenFCM: String)
    fun getTokenFCM():String
}