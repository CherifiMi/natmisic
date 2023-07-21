package com.example.natmisic

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.natmisic.feature.presentation.folder_picker.FolderPickerScreen
import com.example.natmisic.feature.presentation.home.HomeScreen
import com.example.natmisic.feature.presentation.settings.SettingsScreen
import com.example.natmisic.feature.presentation.util.Screens
import com.example.natmisic.feature.presentation.util.SplashScreen
import com.example.natmisic.theme.NatMisicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            !=
            PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            );
        }



        setContent {
            NatMisicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.primary
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screens.SPLASH.name
                    ) {
                        composable(route = Screens.SPLASH.name) {
                            SplashScreen(navController = navController, viewModel)
                        }
                        composable(route = Screens.HOME.name) {
                            HomeScreen(
                                navController,
                                backPressedDispatcher = onBackPressedDispatcher
                            )
                        }
                        composable(route = Screens.FOLDER_PICKER.name) {
                            FolderPickerScreen(navController = navController)
                        }
                        composable(route = Screens.SETTINGS.name) {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

