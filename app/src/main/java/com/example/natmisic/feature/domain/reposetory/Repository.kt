package com.example.natmisic.feature.domain.reposetory

import com.example.natmisic.feature.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getBooks(): Flow<List<Book>>
    suspend fun getBookById(id: Int): Book
    suspend fun insertBook(book: Book)
}