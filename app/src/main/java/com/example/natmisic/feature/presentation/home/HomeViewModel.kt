package com.example.natmisic.feature.presentation.home

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import com.example.natmisic.feature.presentation.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

//states
sealed class HomeEvent {
    data class OpenSettings(val navController: NavController) : HomeEvent()
    data class Init(val context: Context) : HomeEvent()
}

data class HomeState(
    val books: List<Book> = emptyList(),
    val loading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OpenSettings -> {
                event.navController.navigate(Screens.SETTINGS.name)
            }
            is HomeEvent.Init -> {
                _state.value = state.value.copy(books = getBooks(event.context))
            }
        }
    }


    fun getRootFolderName(): String {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY) ?: ""
    }

    fun getBooks(context: Context): List<Book> {
        val uri = Uri.parse(getRootFolderName())
        val documentFile = DocumentFile.fromTreeUri(context, uri)
        val files = documentFile?.listFiles()?.filter { it.type?.startsWith("audio/") == true }


        files?.forEach {
            Log.d(TAG, it.name.toString())
        }

        return files

    }
}
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
