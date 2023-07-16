package com.example.natmisic.feature.presentation.folder_picker

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.text.Html
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startPostponedEnterTransition
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderPickerViewModel @Inject constructor(
    private val useCase: UseCases
) : ViewModel() {
    fun saveRootFolderName(path: String) {
        val s = path
        useCase.setDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY, s)
    }
    fun getRootFolderName(): String {
        return useCase.getDataStoreItem(DataStoreKeys.ROOT_FOLDER_KEY)?:""
    }
}