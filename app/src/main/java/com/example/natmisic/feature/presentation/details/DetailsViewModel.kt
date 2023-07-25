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
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.example.musicplayer.exoplayer.*
import com.example.natmisic.core.exoplayer.MusicService
import com.example.natmisic.core.util.Constants
import com.example.natmisic.core.util.Resource
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.StorageService
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


data class DetailsState(
    val book: Book? = null,
    val prosessing: Boolean = false,
    val popup: Boolean = false,
    val timestamp: Timestamp? = null
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

    data class ShowTimestampPopup(val item: Timestamp) : DetailsEvent()
    object ClosePopup : DetailsEvent()
    data class DeleteTimestamp(val item: Timestamp) : DetailsEvent()
    data class SaveTimestamp(val item: Timestamp, val txt: String) : DetailsEvent()
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
                val startTime = event.timestamp
                val endTime = formatLong(event.timestamp.toTimeDateLong() + 1000 * 10) //10s

                val input = File(book.path)

                val outname = "output${(0..99999).random()}"
                val output = File(context.cacheDir, "$outname.${input.extension}")
                val voskfile = File(context.cacheDir, "$outname.wav")

                _state.value = state.value.copy(prosessing = true)

                viewModelScope.launch(Dispatchers.Default) {
                    val slice10s = FFmpegKit.execute("-i '${input.path}' -ss $startTime -to $endTime -c copy ${output.path}")

                    if (ReturnCode.isSuccess(slice10s.returnCode)) {

                        val formatFile =
                            FFmpegKit.execute("-i '${output.path}' -ar 16000 -ac 1 -f s16le ${voskfile.path}")

                        if (ReturnCode.isSuccess(formatFile.returnCode)) {
                            recognizeFile(voskfile.inputStream(), event.context) {
                                val newBook = book.copy(
                                    timestamp = book.timestamp + Timestamp(
                                        id = book.id!!,
                                        it,
                                        startTime
                                    )
                                )
                                viewModelScope.launch {
                                    useCases.updateBookById(newBook)
                                }
                                _state.value = state.value.copy(book = newBook)
                                Toast.makeText(
                                    context,
                                    "$startTime: $it",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "speech to text went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        _state.value = state.value.copy(prosessing = false)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "cutting file went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                        _state.value = state.value.copy(prosessing = false)
                    }
                    output.delete()
                    voskfile.delete()
                }
            }
            is DetailsEvent.SkipToNextSong -> {}
            is DetailsEvent.SkipToPreviousSong -> {}
            is DetailsEvent.ShowTimestampPopup -> {
                _state.value = state.value.copy(popup = true, timestamp = event.item)
            }
            is DetailsEvent.ClosePopup -> {
                _state.value = state.value.copy(popup = false)
            }
            is DetailsEvent.DeleteTimestamp -> {
                val list = state.value.book!!.timestamp.toMutableList()
                list.remove(event.item)
                val book = state.value.book!!.copy(timestamp = list.toList())
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateBookById(book)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(book = book, popup = false)
                    }
                }
            }
            is DetailsEvent.SaveTimestamp -> {
                val item = event.item
                val newItem = item.copy(text = event.txt)

                val list = state.value.book!!.timestamp.toMutableList()

                list.remove(item)
                list.add(newItem)

                val book = state.value.book!!.copy(timestamp = list.toList())

                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateBookById(book)
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(book = book, popup = false)
                    }
                }
            }
        }
    }


    //region ext
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

    fun String.toTimeDateLong(): Long {
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return format.parse(this)?.time
            ?: throw IllegalArgumentException("Invalid time string")
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

    fun saveProgress(progress: Long, book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateBookById(book.copy(progress = progress.toInt()))
        }
    }


    fun recognizeFile(ais: InputStream, context: Context, spt: (text: String) -> Unit) {
        StorageService.unpack(
            context,
            "model-en-us",
            "model",
            { model: Model? ->
                Recognizer(model, 16000f).use { recognizer ->
                    var nbytes: Int
                    val b = ByteArray(4096)
                    while (ais.read(b).also { nbytes = it } >= 0) {
                        recognizer.acceptWaveForm(b, nbytes)
                    }
                    val stt = JSONObject(recognizer.finalResult).getString("text")
                    spt(stt)
                }
            }
        ) {}
    }

    //endregion

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
    // endregion
}
