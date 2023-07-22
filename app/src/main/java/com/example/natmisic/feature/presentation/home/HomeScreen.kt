package com.example.natmisic.feature.presentation.home

import android.os.Build
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.natmisic.R
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.DetailsEvent
import com.example.natmisic.feature.presentation.details.DetailsScreen2
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
    BackHandler {
        if (detailsViewModel.showPlayerFullScreen){
            detailsViewModel.showPlayerFullScreen = false
        }
    }

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
            TopBar(navController = navController)
            LazyColumn(Modifier.fillMaxSize()) {
                items(state.books) { book ->
                    BookItem(book, detailsViewModel)
                }
                item {
                    Spacer(modifier = Modifier.size(200.dp))
                }
            }
        }
        DetailsBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        DetailsScreen2()
        //DetailsScreen(backPressedDispatcher)
    }
}

@Composable
fun TopBar(navController: NavHostController) {
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {  }) {
            Icon(
                painter = painterResource(id = R.drawable.search_ic),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
        IconButton(onClick = { navController.navigate(Screens.SETTINGS.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.settings_ic),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
@Composable
fun BookItem(book: Book, detailsViewModel: DetailsViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(10.dp))
            .clickable {
                detailsViewModel.onEvent(DetailsEvent.PlayOrToggleSong(book))
                detailsViewModel.showPlayerFullScreen = true
            }
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(4.dp)
                .padding(bottom = 4.dp)
                .background(MaterialTheme.colors.primaryVariant)
                .clip(RoundedCornerShape(10.dp))
            ,
            painter = rememberAsyncImagePainter(model = File(book.cover)),
            contentDescription = ""
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                book.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                book.author,
                fontSize = 13.sp,
                color = MaterialTheme.colors.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.60f
                    }

            )
        }
    }
}