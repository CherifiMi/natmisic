package com.example.natmisic.feature.domain.use_case

import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.reposetory.Repository

class UpdateBookById(
    private val repo: Repository
) {
    suspend operator fun invoke(book: Book){
        repo.insertBook(book)
    }
}