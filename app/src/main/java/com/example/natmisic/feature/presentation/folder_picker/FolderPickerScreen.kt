package com.example.natmisic.feature.presentation.folder_picker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.natmisic.feature.presentation.folder_picker.components.BottomBalls
import com.example.natmisic.feature.presentation.folder_picker.components.PageI
import com.example.natmisic.feature.presentation.folder_picker.components.PageII
import com.example.natmisic.feature.presentation.folder_picker.components.PageIII


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderPickerScreen(
    navController: NavHostController,
    viewModel: FolderPickerViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState()

    Column {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            pageCount = 3,
            state = pagerState
        ) { tabIndex ->
            when (tabIndex) {
                0 -> PageI()
                1 -> PageII()
                2 -> PageIII(navController, viewModel)
            }
        }
        BottomBalls(pagerState)
    }
}








