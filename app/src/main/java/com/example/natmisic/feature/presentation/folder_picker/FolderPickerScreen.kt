package com.example.natmisic.feature.presentation.folder_picker

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker

@Composable
fun FolderPickerScreen(
    viewModel: FolderPickerViewModel = hiltViewModel()
) {



    var showDirPicker by remember { mutableStateOf(false) }

    DirectoryPicker(showDirPicker) { path ->
        showDirPicker = false
        if (path!= null){
            Log.d("FOLDERPICKER", path.toString())
        }
    }

    // primary%3A

    //AB
    // %2F -> /
    // Tony
    // %20 -> " "
    // Fadell
    // %2F
    // Build
    // %20
    // An
    // %20
    // Unorthodox
    // %20
    // Guide
    // %20
    // to
    // %20
    // Making
    // %20
    // Things
    // %20
    // Worth
    // %20
    // Making

    Column(Modifier.fillMaxSize()) {
        Button(onClick = {
            // open folder picker
            showDirPicker = true
            // save rout to data store

            // nave to home
        }) {
            Text(text = "select books folder")
        }
    }
}