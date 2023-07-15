package com.example.natmisic.feature.presentation.folder_picker

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavHostController
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.presentation.util.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@Composable
fun FolderPickerScreen(
    navController: NavHostController,
    viewModel: FolderPickerViewModel = hiltViewModel()
) {
    var showDirPicker by remember { mutableStateOf(false) }

    DirectoryPicker(showDirPicker) { path ->
        showDirPicker = false
        if (path!= null){
            viewModel.saveRootFolderName(path.toString())
            navController.navigate(Screens.HOME.name)
        }
    }

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