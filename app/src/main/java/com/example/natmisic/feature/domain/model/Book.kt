package com.example.natmisic.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "book")
data class Book(
    @PrimaryKey val id: Int? = null,
    val path: String,
    val name: String,
    val author: String,
    val cover: String,
    val duration: Int,
    val progress: Int,
    val timestamp: List<Timestamp?>
)

data class Timestamp(
    val id: Int,
    val text: String,
    val time: String
)

class Converters {
    @TypeConverter
    fun fromTimestamp(timestamp: List<Timestamp?>): String {
        return Gson().toJson(timestamp)
    }

    @TypeConverter
    fun toTimestamp(json: String): List<Timestamp?> {
        return Gson().fromJson<List<Timestamp?>>(json)
    }
}

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)