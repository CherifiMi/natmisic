package com.example.natmisic.feature.presentation.home


import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.musicplayer.exoplayer.isPlaying
import com.example.natmisic.R
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.DetailsEvent
import com.example.natmisic.feature.presentation.details.DetailsViewModel
import java.io.File


@Composable
fun DetailsBottomBar(
    modifier: Modifier = Modifier,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    val currentSong = detailsViewModel.currentPlayingSong.value
    val playbackStateCompat by detailsViewModel.playbackState.observeAsState()


    val colorStops = arrayOf(
        0.0f to Color.Transparent,
        0.2f to MaterialTheme.colors.primary,
        1f to MaterialTheme.colors.primary
    )

    AnimatedVisibility(
        visible = detailsViewModel.state.value.book != null,
        modifier = modifier
    ) {
        if (detailsViewModel.state.value.book != null && currentSong != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Brush.verticalGradient(colorStops = colorStops)),
                contentAlignment = Alignment.BottomCenter
            ) {
                DetailsBottomBarItem(
                    book = detailsViewModel.toBook(currentSong)!!,
                    playbackStateCompat = playbackStateCompat,
                    detailsViewModel = detailsViewModel,
                )
            }
        }
    }
}


@Composable
fun DetailsBottomBarItem(
    book: Book,
    playbackStateCompat: PlaybackStateCompat?,
    detailsViewModel: DetailsViewModel,
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                detailsViewModel.showPlayerFullScreen = true
            }
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .border(BorderStroke(2.dp, MaterialTheme.colors.secondary), RoundedCornerShape(100))
    ) {
        Row(
            Modifier
                .weight(2f)
                .fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colors.primary, RoundedCornerShape(100))
                    .clip(RoundedCornerShape(100)),
                painter = rememberAsyncImagePainter(model = File(book.cover)),
                contentDescription = "",
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    book.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    book.author,
                    fontSize = 12.sp,
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
        Row(
            Modifier
                .weight(1.7f)
                .fillMaxWidth()
                .height(16.dp), horizontalArrangement = Arrangement.SpaceEvenly
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
                    ) {
                        detailsViewModel.currentPlaybackPosition.let { currentPosition ->
                            detailsViewModel.seekTo(if (currentPosition - 15 * 1000f < 0) 0f else currentPosition - 15 * 1000f)
                        }
                    },
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
                    ) { detailsViewModel.playOrToggleSong(book, true) },
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
                    ) {
                        detailsViewModel.currentPlaybackPosition.let { currentPosition ->
                            detailsViewModel.seekTo(currentPosition + 15 * 1000f)
                        }
                    },
            )
            Icon(
                painter = rememberAsyncImagePainter(model = R.drawable.recording),
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
                    ) {
                        detailsViewModel.onEvent(
                            DetailsEvent.RecordAndSaveTranscript(
                                detailsViewModel.toBook(detailsViewModel.currentPlayingSong.value!!)!!,
                                detailsViewModel.currentSongFormattedPosition,
                                context
                            )
                        )
                    },
            )
        }
    }
}

