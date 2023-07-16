package com.example.natmisic.feature.presentation.home

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.example.natmisic.TAG
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {
    fun getRootFolderName(): String {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY)?:""
    }
}

/*val documentFile = DocumentFile.fromTreeUri(this, uri)

                val files = documentFile?.listFiles()?.filter { it.type?.startsWith("audio/") == true }

                files?.forEach {
                    Log.d(TAG, it.name.toString())
                    getMEtaData(it)
                }*/

/*
private fun getMEtaData(file: DocumentFile?) {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(this, file?.uri)
    val title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
    val artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
    val album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
    val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    val cover = mediaMetadataRetriever.embeddedPicture

    Log.d(TAG, "$title   $artist   $album    $duration")
}
*/

/*

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}
* */


/*
data class BookOverviewItemViewState(
    val name: String,
    val author: String?,
    val cover: ImmutableFile?,
    val progress: Float,
    val id: BookId,
    val remainingTime: String,
)
fun Book.progress(): Float {
    val globalPosition = position
    val totalDuration = duration
    val progress = globalPosition.toFloat() / totalDuration.toFloat()
    if (progress < 0F) {
        Logger.w("Couldn't determine progress for book=$this")
    }
    return progress.coerceIn(0F, 1F)
}
*/
