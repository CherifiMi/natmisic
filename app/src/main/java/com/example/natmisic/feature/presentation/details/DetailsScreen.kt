package com.example.natmisic.feature.presentation.details

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.natmisic.R
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.components.CoverTab
import com.example.natmisic.feature.presentation.details.components.MusicController
import com.example.natmisic.feature.presentation.details.components.TranscriptPopUp
import com.example.natmisic.feature.presentation.details.components.TranscriptTab


@ExperimentalMaterialApi
@Composable
fun DetailsScreen(
    backPressedDispatcher: OnBackPressedDispatcher,
    viewmodel: DetailsViewModel = hiltViewModel()
) {
    val state = viewmodel.state.value
    val book = state.book

    AnimatedVisibility(
        visible = book != null && viewmodel.showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )) {
        if (book != null) {
            DetailsBody(
                book = book,
                viewmodel = viewmodel,
                backPressedDispatcher
            )
        }
    }
    AnimatedVisibility(
        visible = state.timestamp != null && state.popup,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TranscriptPopUp(
            item = state.timestamp!!,
            viewmodel = viewmodel,
        )
    }
}



@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun DetailsBody(book: Book, viewmodel: DetailsViewModel, backPressedDispatcher: OnBackPressedDispatcher) {

    val swipeableState = rememberSwipeableState(initialValue = 0)
    val endAnchor = LocalConfiguration.current.screenHeightDp * LocalDensity.current.density
    val anchors = mapOf(
        0f to 0,
        endAnchor to 1
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)

    ) {
        if (swipeableState.currentValue >= 1) {
            LaunchedEffect("key") {
                viewmodel.showPlayerFullScreen = false
            }
        }
        Row(
            Modifier
                .height(56.dp)
                .fillMaxWidth()
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.34f) },
                    orientation = Orientation.Vertical
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { viewmodel.showPlayerFullScreen = false }) {
                Icon(
                    painter = painterResource(id = R.drawable.down_pointer),
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                        .scale(.7f)
                        .padding(horizontal = 16.dp)
                )
            }
            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                        .scale(.7f)
                        .padding(horizontal = 16.dp)
                )
            }
        }
        val pagerState = rememberPagerState()

        Column(Modifier.fillMaxSize()) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                pageCount = 2,
                state = pagerState
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> CoverTab(book.cover)
                    1 -> TranscriptTab(book.timestamp, viewmodel)
                }
            }
            Box(Modifier.weight(1.5f)) {
                MusicController(backPressedDispatcher = backPressedDispatcher)
            }
        }
    }
}
