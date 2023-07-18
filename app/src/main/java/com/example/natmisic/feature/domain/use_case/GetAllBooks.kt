package com.example.natmisic.feature.domain.use_case

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.documentfile.provider.DocumentFile
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.domain.reposetory.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

class GetAllBooks (
    private val repo: Repository,
) {
    suspend operator fun invoke(): List<Book> {
        return repo.getBooks().first()
    }

}