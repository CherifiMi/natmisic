package com.example.musicplayer.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import com.example.musicplayer.exoplayer.State.*
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicSource @Inject constructor(
    private val useCases: UseCases
) {

    var songs = emptyList<MediaMetadataCompat>()
    var songsTest: List<Book> = emptyList()

    suspend fun fetchMediaData() = withContext(Dispatchers.Main) {
        state = STATE_INITIALIZING
        Dispatchers.IO
        val allSongs: List<Book> = useCases.getAllBooks()
        songsTest = allSongs
        songs = allSongs.map { book ->
            MediaMetadataCompat.Builder() // TODO: fix
                .putString(METADATA_KEY_TITLE, book.name)
                .putString(METADATA_KEY_DISPLAY_TITLE, book.name)
                .putString(METADATA_KEY_ARTIST, book.author)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, book.author)
                .putString(METADATA_KEY_MEDIA_ID, book.id.toString()) // ??
                .putString(METADATA_KEY_MEDIA_URI, book.path) // ??
                .putString(METADATA_KEY_DISPLAY_ICON_URI, book.cover) // ??
                .putString(METADATA_KEY_ALBUM_ART_URI, book.cover) // ??
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, book.author)
                .build()
        }
        Dispatchers.Main
        state = STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach { song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.description.mediaUri)
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}