package com.example.natmisic.feature.presentation.home

import android.os.Build
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.natmisic.MainViewModel
import com.example.natmisic.feature.presentation.details.DetailsScreen
import com.example.natmisic.feature.presentation.home.components.LoadingBall
import java.io.File

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    backPressedDispatcher: OnBackPressedDispatcher
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    viewModel.onEvent(HomeEvent.Init(context))

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


    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(state.books) { book ->
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(40.dp)
                        .clickable {
                            mainViewModel.playOrToggleSong(book)
                            mainViewModel.showPlayerFullScreen = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = File(book.cover)),
                        contentDescription = ""
                    )
                    Text(text = book.name)
                }
            }
        }
        DetailsBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)

        )
        DetailsScreen(backPressedDispatcher = backPressedDispatcher)
    }
}