package com.example.natmisic.feature.data.local

import androidx.room.*
import com.example.natmisic.feature.domain.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBookById(id: Int): Book?
}