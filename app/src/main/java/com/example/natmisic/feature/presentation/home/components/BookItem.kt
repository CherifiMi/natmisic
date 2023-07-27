package com.example.natmisic.feature.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.natmisic.R
import com.example.natmisic.feature.domain.model.Book
import com.example.natmisic.feature.presentation.details.DetailsEvent
import com.example.natmisic.feature.presentation.details.DetailsViewModel
import com.example.natmisic.theme.Blu
import com.example.natmisic.theme.Grn
import java.io.File

@Composable
fun BookItem(book: Book, detailsViewModel: DetailsViewModel) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp)
        .padding(horizontal = 8.dp)
        .background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .clickable(enabled = !detailsViewModel.showPlayerFullScreen){
            detailsViewModel.onEvent(DetailsEvent.PlayOrToggleSong(book))
            if (detailsViewModel.state.value.book?.id != book.id) {
                detailsViewModel.seekTo(book.progress.toFloat())
            }
            detailsViewModel.showPlayerFullScreen = true
        }
    ) {
        Row(Modifier.fillMaxSize())
        {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .padding(4.dp)
                    .padding(bottom = 4.dp)
                    .background(MaterialTheme.colors.primaryVariant)
                    .clip(RoundedCornerShape(10.dp)),
                painter = if(book.cover != "") rememberAsyncImagePainter(model = File(book.cover)) else painterResource(
                    R.drawable.defult_cover),
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
        if (book.progress >= 1000 * 60 * 1) {
            Column(
                Modifier
                    .height(132.dp)
                    .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text =
                    if (book.duration - book.progress < 1000 * 60 * 1) "done"
                    else "${
                        (detailsViewModel.formatLong((book.duration - book.progress).toLong())).substring(
                            0,
                            5
                        ).replace(":", "h")
                    }min left",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 16.dp, bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .drawBehind {
                            drawRect(
                                if (book.duration - book.progress < 1000 * 60 * 1) Grn else Blu,
                                size = Size(
                                    size.width * (book.progress.toFloat() / book.duration.toFloat()),
                                    size.height
                                )
                            )
                        }
                )
            }
        }
    }
}