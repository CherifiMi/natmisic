package com.example.natmisic.feature.presentation.home


import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.exoplayer.isPlaying
import com.example.natmisic.MainViewModel
import com.example.natmisic.R
import com.example.natmisic.core.exoplayer.toBook
import com.example.natmisic.core.util.TAG
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.DetailsViewModel
import java.io.File


@Composable
fun DetailsBottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    var offsetX by remember { mutableStateOf(0f) }
    val currentSong = viewModel.currentPlayingSong.value
    val playbackStateCompat by viewModel.playbackState.observeAsState()

    AnimatedVisibility(
        visible = currentSong != null,
        modifier = modifier
    ) {
        if (currentSong != null) {
            val song = currentSong.toBook()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val (x, y) = dragAmount
                                offsetX = x
                            }
                        )

                    }
                    .background(MaterialTheme.colors.primary)
                    .clickable {
                        viewModel.showPlayerFullScreen = true
                    },
            ) {
                DetailsBottomBarItem(
                    book = song!!,
                    playbackStateCompat = playbackStateCompat,
                    viewModel = viewModel,
                    detailsViewModel = detailsViewModel
                )
            }
        }
    }
}


@Composable
fun DetailsBottomBarItem(
    book: Book,
    playbackStateCompat: PlaybackStateCompat?,
    viewModel: MainViewModel,
    detailsViewModel: DetailsViewModel
) {
    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also {
            Log.d(TAG, it.data.toString())
        }
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(BorderStroke(2.dp, MaterialTheme.colors.secondary), RoundedCornerShape(100))
    ) {
        Row(Modifier.weight(2f)) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .padding(8.dp)
                    .background(MaterialTheme.colors.primary, RoundedCornerShape(100)),
                painter = rememberAsyncImagePainter(model = File(book.cover)),
                contentDescription = ""
            )
            Column(

                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
            ) {
                Text(
                    book.name,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    book.author,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = 0.60f
                        }

                )
            }
        }
        Row(Modifier.weight(1.7f), horizontalArrangement = Arrangement.SpaceEvenly) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.play_back_ic),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewModel.playOrToggleSong(book, true) },
            )
            Image(
                painter = rememberAsyncImagePainter(
                    model = if (playbackStateCompat?.isPlaying == false) {
                        R.drawable.play_ic
                    } else {
                        R.drawable.play_puase_ic
                    }
                ),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewModel.playOrToggleSong(book, true) },
            )
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.play_skip_ic),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewModel.playOrToggleSong(book, true) },
            )
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.recording),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) {
                        detailsViewModel.recordAndSaveTranscript(
                            viewModel.currentPlayingSong.value!!.toBook()!!,
                            detailsViewModel.currentPlayerPosition,
                            activityResultLauncher
                        )
                    },
            )
        }
    }
}

