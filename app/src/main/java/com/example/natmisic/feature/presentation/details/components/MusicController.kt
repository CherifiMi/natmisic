package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.musicplayer.exoplayer.isPlaying
import com.example.natmisic.R
import com.example.natmisic.feature.presentation.details.DetailsEvent
import com.example.natmisic.feature.presentation.details.DetailsViewModel

@Composable
fun MusicController(viewmodel: DetailsViewModel = hiltViewModel()) {
    val state = viewmodel.state.value
    var sliderIsChanging by remember { mutableStateOf(false) }
    var localSliderValue by remember { mutableStateOf(0f) }
    var sliderProgress =
        if (sliderIsChanging) localSliderValue else viewmodel.currentPlayerPosition
    val sliderColors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colors.secondary,
        activeTrackColor = MaterialTheme.colors.secondary,
        inactiveTrackColor = if (isSystemInDarkTheme())
            MaterialTheme.colors.onBackground.copy(
                alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity
            ) else MaterialTheme.colors.secondary.copy(alpha = ProgressIndicatorDefaults.IndicatorBackgroundOpacity)
    )
    val book = state.book
    val context = LocalContext.current
    val playbackStateCompat by viewmodel.playbackState.observeAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    viewmodel.currentPlaybackFormattedPosition,
                    style = MaterialTheme.typography.body2
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    viewmodel.currentSongFormattedPosition,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Slider(
            value = sliderProgress,
            modifier = Modifier
                .fillMaxWidth(),
            colors = sliderColors,
            onValueChange = { newPosition ->
                localSliderValue = newPosition
                sliderIsChanging = true
            },
            onValueChangeFinished = {
                sliderProgress = localSliderValue
                viewmodel.seekTo(viewmodel.currentSongDuration * localSliderValue)
                viewmodel.saveProgress(viewmodel.currentPlaybackPosition, book!!)
                sliderIsChanging = false
            }
        )
        Text(
            text = state.book!!.name,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            state.book.author,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.graphicsLayer {
                alpha = 0.60f
            }
        )
        Row(
            Modifier
                .weight(1.7f)
                .fillMaxWidth()
                .height(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.play_back_ic),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .scale(0.9f)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewmodel.onEvent(DetailsEvent.Back) },
            )
            Icon(
                painter = rememberAsyncImagePainter(
                    model = if (playbackStateCompat?.isPlaying == false) {
                        R.drawable.play_ic
                    } else {
                        R.drawable.play_puase_ic
                    }
                ),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewmodel.onEvent(DetailsEvent.PlayOrToggleSong(book!!, true)) },
            )
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.play_skip_ic),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .scale(0.9f)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewmodel.onEvent(DetailsEvent.Skip) },
            )
            val composition by rememberLottieComposition(
                if (!isSystemInDarkTheme()) LottieCompositionSpec.RawRes(
                    R.raw.lr_dark
                ) else LottieCompositionSpec.RawRes(R.raw.lr_light)
            )
            IconButton(
                modifier = Modifier
                    .padding(end = 16.dp),
                onClick = {
                    viewmodel.onEvent(
                        DetailsEvent.RecordAndSaveTranscript(
                            viewmodel.toBook(viewmodel.currentPlayingSong.value!!)!!,
                            viewmodel.currentSongFormattedPosition,
                            context
                        )
                    )
                }
            ) {
                LottieAnimation(
                    composition = composition,
                    isPlaying = viewmodel.state.value.prosessing,
                    restartOnPlay = true,
                    reverseOnRepeat = true,
                    iterations = LottieConstants.IterateForever,
                    alignment = Alignment.Center,
                    modifier = Modifier.scale(2.5f)
                )

            }
        }

    }
}