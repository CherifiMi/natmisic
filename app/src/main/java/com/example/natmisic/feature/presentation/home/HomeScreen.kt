package com.example.natmisic.feature.presentation.home

import android.os.Build
import androidx.activity.OnBackPressedDispatcher
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.DetailsScreen
import com.example.natmisic.feature.presentation.details.DetailsViewModel
import com.example.natmisic.feature.presentation.home.components.LoadingBall
import com.example.natmisic.feature.presentation.util.Screens
import java.io.File


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
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
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.height(80.dp).clickable { navController.navigate(Screens.SETTINGS.name) }
            ){
                Text(text = "search bar")
            }
            LazyColumn(Modifier.fillMaxSize()) {
                items(state.books) { book ->
                    BookItem(book, detailsViewModel)
                }
            }
        }
        DetailsBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        DetailsScreen(backPressedDispatcher = backPressedDispatcher)
    }
}

@Composable
fun BookItem(book: Book, mainViewModel: DetailsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(4.dp))
            .clickable {
                mainViewModel.playOrToggleSong(book)
                mainViewModel.showPlayerFullScreen = true
            }
    ) {
        Image(
            modifier = Modifier
                .size(112.dp)
                .padding(4.dp)
                .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp)),
            painter = rememberAsyncImagePainter(model = File(book.cover)),
            contentDescription = ""
        )
        Column(Modifier.fillMaxSize()) {
            Text(text = book.name, color = MaterialTheme.colors.secondary)
            Text(text = book.name, color = MaterialTheme.colors.secondary)
        }
    }
}