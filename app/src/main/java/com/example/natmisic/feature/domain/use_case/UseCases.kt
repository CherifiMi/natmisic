package com.example.natmisic.feature.domain.use_case

data class UseCases(
    val setDataStoreItem: SetDataStoreItem,
    val getDataStoreItem: GetDataStoreItem,
    val updateAndGetBooks: UpdateAndGetBooks,
    val getAllBooks: GetAllBooks,
    val getBookById: GetBookById,
    val updateBookById: UpdateBookById,
)