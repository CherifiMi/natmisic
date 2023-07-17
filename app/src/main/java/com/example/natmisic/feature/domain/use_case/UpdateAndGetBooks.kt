package com.example.natmisic.feature.domain.use_case

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
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

class UpdateAndGetBooks(
    private val repo: Repository,
    private val dataStore: DataStore<Preferences>,
) {
    suspend operator fun invoke(context: Context): Flow<List<Book>> {

        val path =
            runBlocking {
                dataStore.data.map {
                    it[DataStoreKeys.ROOT_FOLDER_KEY]
                }.first()
            } ?: ""

        val uri = Uri.parse(path)
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        val files = documentFile?.listFiles()?.filter { it.type?.startsWith("audio/") == true }

        files?.forEach {file->
            val book = fileToBook(file, context)
            if (repo.getBooks().first().none { it.name == book.name }){
                repo.insertBook(book)
            }
        }

        return repo.getBooks()
    }

    private fun fileToBook(file: DocumentFile, context: Context): Book {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, file.uri)
        val name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val author = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val cover = mediaMetadataRetriever.embeddedPicture

        // TODO: convert cover to path

        return Book(
         path = file.uri.lastPathSegment!!,
         name = name ?: "",
         author = author ?: "",
         cover = "",
         duration = duration?.toInt() ?: 0,
         progress = 0,
         timestamp = emptyList<Timestamp>()
        )
    }
}