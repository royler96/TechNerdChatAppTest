package com.basemp.chatapptechnerd.storage

import android.content.Context

interface IMStorageInjection {
    fun setUp(app: Context)

    fun sharedPreferenceManager():Any?
}