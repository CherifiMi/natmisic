package com.example.natmisic.feature.presentation.folder_picker

import android.text.Html
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.natmisic.core.util.DataStoreKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderPickerViewModel @Inject constructor(
    //private val noteUseCase: NoteUseCase
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    fun saveRootFolderName(s: String){
        viewModelScope.launch {
            dataStore.edit {
                it[DataStoreKeys.ROOT_FOLDER_KEY] = s
            }
        }
    }
    fun getRootFolderName(){
        viewModelScope.launch {
            dataStore.data.collectLatest {
                val root = it[DataStoreKeys.ROOT_FOLDER_KEY] ?: "nothing"
                val decoded: String = Html
                    .fromHtml(root, Html.FROM_HTML_MODE_COMPACT)
                    .toString()
                Log.d("FOLDERPICKER", decoded)
            }
        }
    }
}