package com.example.natmisic.feature.data

import com.example.natmisic.feature.data.local.BookDao
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.reposetory.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImp(private val dao: BookDao): Repository {
    override fun getBooks(): Flow<List<Book>> {
        return dao.getBooks()
    }

    override suspend fun getBookById(id: Int): Book {
        return dao.getBookById(id)!!
    }

    override suspend fun insertBook(book: Book) {
        dao.insertBook(book)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}