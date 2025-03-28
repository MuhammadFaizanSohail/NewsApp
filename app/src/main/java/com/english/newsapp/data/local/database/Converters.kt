package com.english.newsapp.data.local.database

import androidx.room.TypeConverter
import com.english.newsapp.data.model.Source
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromSourceList(source: List<Source>): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return Gson().fromJson(sourceString, Source::class.java)
    }
}