package com.example.natmisic.feature.presentation.home

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.musicplayer.exoplayer.MusicServiceConnection
import com.example.natmisic.core.exoplayer.MusicService
import com.example.natmisic.core.util.Constants
import com.example.natmisic.core.util.Resource
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import com.example.natmisic.feature.presentation.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class HomeEvent {
    data class OpenSettings(val navController: NavController) : HomeEvent()
    object UpdateState: HomeEvent()
}

data class HomeState(
    val books: List<Book> = emptyList(),
    val loading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: UseCases,
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    var mediaItems = mutableStateOf<Resource<List<Book>>>(Resource.Loading(null))

    init {

        Log.d(TAG, "1")

        val books = runBlocking(Dispatchers.IO){
            useCase.updateAndGetBooks().first()
        }
        mediaItems.value = (Resource.Loading(null))
        runBlocking(Dispatchers.IO) {

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
                                progress = 9999,
                                timestamp = emptyList()
                            )
                        }
                        mediaItems.value = Resource.Success(items)
                    }
                }
            )
        }

        Log.d(TAG, "5")
        _state.value = state.value.copy(books = books, loading = false)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OpenSettings -> {
                event.navController.navigate(Screens.SETTINGS.name)
            }
            HomeEvent.UpdateState -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.getAllBooks().let {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(books = it)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            Constants.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
        val service = MusicService()
        service.exoPlayer.clearMediaItems()
        service.exoPlayer.release()
    }

}


