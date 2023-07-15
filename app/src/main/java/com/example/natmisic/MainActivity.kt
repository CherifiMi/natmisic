package com.example.natmisic

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.natmisic.feature.presentation.folder_picker.FolderPickerScreen
import com.example.natmisic.feature.presentation.home.HomeScreen
import com.example.natmisic.feature.presentation.util.Screens
import com.example.natmisic.theme.NatMisicTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NatMisicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if(viewModel.hasRootFolder()) Screens.HOME.name else Screens.FOLDER_PICKER.name
                    ) {
                        composable(route = Screens.HOME.name) {
                            HomeScreen(navController)
                        }
                        composable(route = Screens.FOLDER_PICKER.name) {
                            FolderPickerScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
