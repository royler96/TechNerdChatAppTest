package com.basemp.chatapptechnerd.helpers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject

object GsonHelper {
    private val gsonBuilder: GsonBuilder

    private val gson: Gson

    init {
        gsonBuilder= GsonBuilder()
        gson= gsonBuilder.create()
    }

    fun generateJSONObject(obj: Any): JSONObject? {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(gson.toJson(obj))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonObject
    }

    fun <T> convertJsonToClass(json: String, cls: Class<T>): T {
        return gson.fromJson(json, cls)
    }
}