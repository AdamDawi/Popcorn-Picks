package com.adamdawi.popcornpicks.core.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListToString(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(json: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(json, type)
    }
}
