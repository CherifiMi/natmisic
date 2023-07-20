package com.example.natmisic.feature.presentation.folder_picker.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.BottomBalls(state: PagerState, color: Color = MaterialTheme.colors.secondary) {
    Row(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .alpha(if (state.currentPage == 0) 1f else 0.25f)
                .background(color, RoundedCornerShape(100))
                .size(13.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .alpha(if (state.currentPage == 1) 1f else 0.25f)
                .background(color, RoundedCornerShape(100))
                .size(13.dp)

        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .alpha(if (state.currentPage == 2) 1f else 0.25f)
                .background(color, RoundedCornerShape(100))
                .size(13.dp)
        )
    }

}