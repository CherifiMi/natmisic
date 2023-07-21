package com.example.natmisic.feature.presentation.details

import android.content.Context
import android.os.Environment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.musicplayer.exoplayer.*
import com.example.natmisic.core.exoplayer.MusicService
import com.example.natmisic.core.util.Constants
import com.example.natmisic.core.util.Resource
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val useCases: UseCases
) : ViewModel() {

    fun recordAndSaveTranscript(
        book: Book,
        timestamp: Float,
        context: Context
    ) {
        Log.d(TAG, timestamp.toString())
        // TODO: get audio slice
         val path = Environment.getExternalStorageDirectory().path + "/Autodesk/Build.m4a"
        val out = File(context.cacheDir, "output2.m4a").path

        val rc = FFmpeg.execute("-i $path -ss 00:01:20 -to 00:01:30 -c copy $out")

        viewModelScope.launch (Dispatchers.IO){
            if (rc == RETURN_CODE_SUCCESS) {
                Log.d(TAG, "Command execution completed successfully.")
            } else if (rc == RETURN_CODE_CANCEL) {
                Log.d(TAG, "Command execution cancelled by user.")
            } else {
                Log.d(
                    TAG,
                    String.format("Command execution failed with rc=%d and the output below.", rc)
                )
            }
        }

        // TODO: get text in that slice


        // TODO: save to timestamp note
        val stt = "this ais a test"
        val newBook = book.copy(
            timestamp = book.timestamp + Timestamp(
                id = book.id!!,
                stt,
                timestamp.toLong()
            )
        )
        runBlocking(Dispatchers.IO) {
            useCases.updateBookById(newBook)
        }

        // TODO: update the ui state

    }

    // region exoplyer
    var currentPlaybackPosition by mutableStateOf(0L)

    val currentPlayerPosition: Float
        get() {
            if (currentSongDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentSongDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentSongFormattedPosition: String
        get() = formatLong(currentSongDuration)


    val currentSongDuration: Long
        get() = MusicService.currentSongDuration

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPlaybackPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(Constants.UPDATE_PLAYER_POSITION_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(value)
    }


    var mediaItems = mutableStateOf<Resource<List<Book>>>(Resource.Loading(null))

    var showPlayerFullScreen by mutableStateOf(false)

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkFailure
    val curPlayingSong = musicServiceConnection.nowPlaying

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    val songIsPlaying: Boolean
        get() = playbackState.value?.isPlaying == true

    val playbackState = musicServiceConnection.playbackState


    init {
        mediaItems.value = (Resource.Loading(null))
        musicServiceConnection.subscribe(
            Constants.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {
                        Book(
                            id = it.mediaId!!.toInt(),
                            path = it.description.mediaUri.toString(),
                            name = it.description.title.toString(),
                            author = it.description.subtitle.toString(),
                            cover = it.description.iconUri.toString(),
                            duration = MusicService.currentSongDuration.toInt(),
                            progress = 0,
                            timestamp = emptyList()
                        )
                    }
                    mediaItems.value = Resource.Success(items)
                }
            }
        )
    }


    fun skipToNextSong() {
        musicServiceConnection.transportController.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportController.skipToPrevious()
    }

    fun seekTo(pos: Float) {
        musicServiceConnection.transportController.seekTo(pos.toLong())
    }

    fun playOrToggleSong(mediaItem: Book, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.id.toString() ==
            currentPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
//            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)
        ) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> {
                        if (toggle) musicServiceConnection.transportController.pause()
                    }
                    playbackState.isPlayEnabled -> {
                        musicServiceConnection.transportController.play()
                    }
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportController.playFromMediaId(
                mediaItem.id.toString(),
                null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            Constants.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }

    fun toBook(currentSong: MediaMetadataCompat): Book? {
        return currentSong.description.let {
            runBlocking(Dispatchers.IO) {
                useCases.getBookById(it.mediaId!!.toInt())
            }
        }
    }
    // endregion
}