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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.domain.use_case.UseCases
import com.example.natmisic.feature.presentation.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.updateAndGetBooks(event.context).collect{
                        launch(Dispatchers.Main) {
                            _state.value = state.value.copy(books = it)
                        }
                    }
                }
            }
        }
    }
    fun getBooks(context: Context): List<Book> {

        return emptyList()
    }
}

