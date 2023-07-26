package com.example.natmisic.feature.presentation.home

import android.os.Build
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.natmisic.feature.presentation.details.DetailsScreen
import com.example.natmisic.feature.presentation.details.DetailsViewModel
import com.example.natmisic.feature.presentation.home.components.BookItem
import com.example.natmisic.feature.presentation.home.components.LoadingBall
import com.example.natmisic.feature.presentation.home.components.TopBar


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    backPressedDispatcher: OnBackPressedDispatcher
) {
    BackHandler {
        if (detailsViewModel.showPlayerFullScreen) {
            detailsViewModel.showPlayerFullScreen = false
        }
    }

    LaunchedEffect(detailsViewModel.currentPlaybackFormattedPosition) {
        if (detailsViewModel.currentPlaybackFormattedPosition.takeLast(2) == "00") {
            detailsViewModel.currentPlayingSong.value?.let { detailsViewModel.toBook(it) }?.let {
                detailsViewModel.saveProgress(detailsViewModel.currentPlaybackPosition, it)
            }
        }
    }

    val state = viewModel.state.value


    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(Modifier.fillMaxSize()) {
            TopBar(navController = navController)
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .alpha(if (state.loading) 0f else 1f)
            ) {
                item {
                    Text(
                        text = "Library",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(state.books.sortedBy { it.progress / it.duration }.reversed()) { book ->
                    BookItem(book, detailsViewModel)
                }
                item {
                    Spacer(modifier = Modifier.size(200.dp))
                }
            }
        }
        DetailsBottomBar(modifier = Modifier.align(Alignment.BottomCenter), backPressedDispatcher)
        DetailsScreen(backPressedDispatcher)
    }

    AnimatedVisibility(
        visible = state.loading,
        enter = fadeIn(),
        exit = fadeOut() + scaleOut(targetScale = .1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingBall()
        }
    }
}

