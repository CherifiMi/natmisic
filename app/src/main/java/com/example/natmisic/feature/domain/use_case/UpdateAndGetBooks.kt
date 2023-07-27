package com.example.natmisic.feature.domain.use_case

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.documentfile.provider.DocumentFile
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.domain.reposetory.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream

class UpdateAndGetBooks(
    private val repo: Repository,
    private val dataStore: DataStore<Preferences>,
    private val context: Context
) {
    suspend operator fun invoke(): Flow<List<Book>> {

        Log.d(TAG, "2")

        val path = context.contentResolver.persistedUriPermissions.firstOrNull()!!.uri.toString()

        val documentFile = DocumentFile.fromTreeUri(context, Uri.parse(path))
        val files = documentFile?.listFiles()?.filter { it.type?.startsWith("audio/") == true } // not getting files????

        val oldBooksInDB = repo.getBooks().first()
        val newBooks = files?.map { fileToBook(it, context) }
        repo.deleteAll()

        newBooks?.forEach { b->
            if (!oldBooksInDB.any { it.path == b.path }){
                repo.insertBook(b)
            }
        }
        oldBooksInDB.forEach { B ->
            if (newBooks != null && newBooks.isNotEmpty()){
                if (newBooks.any { B.path == it.path }){
                    repo.insertBook(B)
                }
            }
        }

        return repo.getBooks()
    }

    private fun fileToBook(file: DocumentFile, context: Context): Book {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, file.uri)
        val name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val author =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val duration =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val cover = coverToPath(mediaMetadataRetriever.embeddedPicture, name ?: "", context)

        return Book(
            path = Environment.getExternalStorageDirectory().absolutePath + file.uri.lastPathSegment?.replace(
                "primary:",
                "/"
            ),
            name = name ?: "",
            author = author ?: "",
            cover = cover ?: "",
            duration = duration?.toInt() ?: 0,
            progress = 0,
            timestamp = emptyList<Timestamp>()
        )
    }

    fun coverToPath(byteArray: ByteArray?, name: String, context: Context): String? {
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            val file = File(context.cacheDir, "$name.jpg")
            val outputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file.path
        }
        return null
    }
}