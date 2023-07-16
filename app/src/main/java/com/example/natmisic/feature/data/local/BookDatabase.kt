package com.example.natmisic.feature.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Converters

@Database(
    entities = [Book::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BookDatabase: RoomDatabase() {
    abstract val bookDao: BookDao
    companion object{
        const val Database_Name = "database"
    }
}