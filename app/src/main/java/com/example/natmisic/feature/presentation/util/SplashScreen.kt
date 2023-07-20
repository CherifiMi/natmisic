package com.example.natmisic.feature.presentation.util

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.natmisic.MainViewModel
import com.example.natmisic.R
import kotlinx.coroutines.delay

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