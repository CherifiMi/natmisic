package com.example.natmisic

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract.*
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.natmisic.feature.presentation.folder_picker.FolderPickerScreen
import com.example.natmisic.feature.presentation.home.HomeScreen
import com.example.natmisic.feature.presentation.settings.SettingsScreen
import com.example.natmisic.feature.presentation.util.Screens
import com.example.natmisic.theme.NatMisicTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

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

@Composable
fun SplashScreen(navController: NavHostController, viewModel: MainViewModel) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(durationMillis = 500, easing = {
                OvershootInterpolator(2f).getInterpolation(it)
            })
        )
        delay(2000)
        //navController.navigate(if (viewModel.hasRootFolder()) Screens.HOME.name else Screens.FOLDER_PICKER.name)
        navController.navigate(Screens.FOLDER_PICKER.name)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.splash_light else R.drawable.splash_dark),
            contentDescription = "",
            modifier = Modifier.scale(scale.value)
        )
    }
}
