package com.example.natmisic.feature.presentation.details

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.example.musicplayer.exoplayer.*
import com.example.natmisic.core.exoplayer.MusicService
import com.example.natmisic.core.util.Constants
import com.example.natmisic.core.util.Resource
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class DetailsState(
    val book: Book? = null,
    val prosessing: Boolean = false
)

sealed class DetailsEvent {
    data class PlayOrToggleSong(val mediaItem: Book, val toggle: Boolean = false) : DetailsEvent()
    object Back : DetailsEvent()
    object Skip : DetailsEvent()
    object SkipToNextSong : DetailsEvent()
    object SkipToPreviousSong : DetailsEvent()
    data class RecordAndSaveTranscript(
        val book: Book,
        val timestamp: String,
        val context: Context
    ) : DetailsEvent()
}


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val useCases: UseCases
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.PlayOrToggleSong -> {
                val mediaItem = event.mediaItem
                val toggle = event.toggle

                updateCurrentBookState(mediaItem.id!!)
                val isPrepared = playbackState.value?.isPrepared ?: false
                if (isPrepared && mediaItem.id.toString() ==
                    currentPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
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
            is DetailsEvent.Skip -> {
                currentPlaybackPosition.let { currentPosition ->
                    seekTo(currentPosition + 10 * 1000f)
                }
            }
            is DetailsEvent.Back -> {
                currentPlaybackPosition.let { currentPosition ->
                    seekTo(if (currentPosition - 10 * 1000f < 0) 0f else currentPosition - 10 * 1000f)
                }
            }
            is DetailsEvent.RecordAndSaveTranscript -> {
                val book = event.book
                val context = event.context
                val timestamp = event.timestamp

                val input = File(book.path)
                val output =
                    File(context.cacheDir, "output${(0..99999).random()}.${input.extension}")

                _state.value = state.value.copy(prosessing = true)

                viewModelScope.launch(Dispatchers.Default) {
                    val rc =
                        FFmpeg.execute("-i '${input.path}' -ss 00:01:20 -to 00:01:30 -c copy ${output.path}")
                    when (rc) {
                        RETURN_CODE_SUCCESS -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Note saved at $timestamp",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // TODO: use stt

                            val stt =
                                "I've tried this but the speechRecognizer stops recognizing after the first listen or doesn't listen at all sometimes"

                            val newBook = book.copy(
                                timestamp = book.timestamp + Timestamp(
                                    id = book.id!!,
                                    stt,
                                    timestamp
                                )
                            )
                            useCases.updateBookById(newBook)
                            updateCurrentBookState(newBook.id!!)

                            output.delete()
                            _state.value = state.value.copy(prosessing = false)
                        }
                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            output.delete()
                            _state.value = state.value.copy(prosessing = false)
                        }
                    }
                }
            }
            DetailsEvent.SkipToNextSong -> {}
            DetailsEvent.SkipToPreviousSong -> {}
        }
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

    var mediaItems = mutableStateOf<Resource<List<Book>>>(Resource.Loading(null))

    var showPlayerFullScreen by mutableStateOf(false)

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

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPlaybackPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(Constants.UPDATE_PLAYER_POSITION_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(value)
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
        updateCurrentBookState(mediaItem.id!!)
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

    fun updateCurrentBookState(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = useCases.getBookById(id)
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(book = book)
            }
        }
    }

    fun saveProgress(progress: Long, book: Book){
        viewModelScope.launch (Dispatchers.IO){
            useCases.updateBookById(book.copy(progress = progress.toInt()))
        }
    }
    // endregion
}
