package com.example.natmisic.feature.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.natmisic.feature.presentation.home.components.LoadingBall

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    viewModel.onEvent(HomeEvent.Init(context))

    AnimatedVisibility(
        visible = state.loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoadingBall()
    }


    LazyColumn(Modifier.fillMaxSize()) {
        items(state.books) {book->
            Text(text = book.name)
        }
    }
}