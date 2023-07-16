package com.example.natmisic.feature.presentation.folder_picker

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.natmisic.feature.presentation.util.Screens

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
            viewModel.saveRootFolderName(uri.dataString.toString())
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