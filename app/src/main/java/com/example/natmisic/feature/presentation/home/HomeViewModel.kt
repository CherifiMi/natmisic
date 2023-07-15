package com.example.natmisic.feature.presentation.home

import android.text.Html
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natmisic.core.util.DataStoreKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    //private val noteUseCase: NoteUseCase
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    fun getRootFolderName(): String = runBlocking {
        dataStore.data.map {
            it[DataStoreKeys.ROOT_FOLDER_KEY] ?: "null"
        }.first()
    }
}