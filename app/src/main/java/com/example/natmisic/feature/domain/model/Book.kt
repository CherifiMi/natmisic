package com.example.natmisic.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "book")
data class Book(
    @PrimaryKey val id: Int? = null,
    val path: String?,
    val name: String?,
    val author: String?,
    val cover: String?,
    val duration: Long?,
    val progress: Long?,
    val timestamp: List<Timestamp?>
) {
    /*fun Book.progress(): Float {
        val globalPosition = 5 //position
        val totalDuration = duration
        val progress = globalPosition.toFloat() / totalDuration!!.toFloat()

        return progress.coerceIn(0F, 1F)
    }*/
    /*fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }*/
}

data class Timestamp(
    val id: Int,
    val text: String,
    val time: Long
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