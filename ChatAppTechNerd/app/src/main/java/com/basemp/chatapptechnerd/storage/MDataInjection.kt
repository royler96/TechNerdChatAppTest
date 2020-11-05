package com.basemp.chatapptechnerd.storage

import android.content.Context
import androidx.annotation.NonNull
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref

object MDataInjection : IMStorageInjection {
    override fun setUp(@NonNull context: Context) {
        MDefaultSharedPref.instance.build(context)
    }


    override fun sharedPreferenceManager(): Any? {
        return  MDefaultSharedPref.instance
    }


    fun instance():MDataInjection{
        return this
    }
}