package com.example.natmisic

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.musicplayer.exoplayer.isPlayEnabled
import com.example.musicplayer.exoplayer.isPlaying
import com.example.musicplayer.exoplayer.isPrepared
import com.example.natmisic.core.exoplayer.MusicService
import com.example.natmisic.core.util.Constants
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.core.util.Resource
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: UseCases,
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {
    // TODO: run in flash screen
    fun hasRootFolder(): Boolean {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY) != null
    }

    // for exoplyer move all to details screen

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
            musicServiceConnection.transportController.playFromMediaId(mediaItem.id.toString(), null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            Constants.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}