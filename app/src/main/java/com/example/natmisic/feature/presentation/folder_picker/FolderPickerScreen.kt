package com.example.natmisic.feature.presentation.folder_picker

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.natmisic.feature.presentation.util.Screens
import com.google.accompanist.pager.ExperimentalPagerApi


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
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
                .weight(3f),
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

@Composable
fun PageI() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}

@Composable
fun PageII() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)
    )
}

@Composable
fun PageIII(
    navController: NavHostController,
    viewModel: FolderPickerViewModel
) {
    // open folder picker and save root path
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also { uri ->
            viewModel.saveRootFolderName(uri.dataString.toString())
            navController.navigate(Screens.HOME.name)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Button(onClick = { activityResultLauncher.launch(intent) })
        {
            Text(text = "select books folder")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.BottomBalls(state: PagerState) {
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
                .background(Color.Blue, RoundedCornerShape(100))
                .size(16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .alpha(if (state.currentPage == 1) 1f else 0.25f)
                .background(Color.Blue, RoundedCornerShape(100))
                .size(16.dp)

        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .alpha(if (state.currentPage == 2) 1f else 0.25f)
                .background(Color.Blue, RoundedCornerShape(100))
                .size(16.dp)
        )
    }

}

