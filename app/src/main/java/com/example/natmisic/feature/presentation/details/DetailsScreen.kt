package com.example.natmisic.feature.presentation.details

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.natmisic.MainViewModel



@ExperimentalMaterialApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailsScreen(
    backPressedDispatcher: OnBackPressedDispatcher,
    mainViewModel: MainViewModel = hiltViewModel(),
    //songViewModel: SongViewModel = hiltViewModel()
) {
    val song = mainViewModel.currentPlayingSong.value
    AnimatedVisibility(
        visible = song != null && mainViewModel.showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )) {
        if (song != null) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Red))
        }
    }
}