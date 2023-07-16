package com.example.natmisic.feature.data.local

import android.provider.ContactsContract
import androidx.room.*
import com.example.natmisic.feature.domain.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: Book)
}