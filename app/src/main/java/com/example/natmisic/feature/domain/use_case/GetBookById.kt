package com.example.natmisic.feature.domain.use_case

import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.reposetory.Repository

class GetBookById(
    private val repo: Repository,
) {
    suspend operator fun invoke(id: Int): Book {
        return repo.getBookById(id)
    }

}
