package com.example.natmisic

import android.text.Html
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: UseCases,
) : ViewModel() {
    // TODO: run in flash screen
    fun hasRootFolder(): Boolean {
        return useCase.getRootFolderName() != ""
    }
}
