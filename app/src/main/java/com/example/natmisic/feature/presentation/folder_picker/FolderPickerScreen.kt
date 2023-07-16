package com.example.natmisic.feature.presentation.folder_picker

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.example.natmisic.TAG
import com.example.natmisic.core.util.DataStoreKeys
import com.example.natmisic.feature.presentation.util.Screens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@Composable
fun FolderPickerScreen(
    navController: NavHostController,
    viewModel: FolderPickerViewModel = hiltViewModel(),
) {

    // open folder picker and save root path
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also { uri ->
            viewModel.saveRootFolderName(uri.toString())
            navController.navigate(Screens.HOME.name)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Button(onClick = { activityResultLauncher.launch(intent) })
        {
            Text(text = "select books folder")
        }
    }
}