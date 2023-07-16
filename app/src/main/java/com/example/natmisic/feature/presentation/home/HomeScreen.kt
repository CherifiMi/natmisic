package com.example.natmisic.feature.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LazyColumn(
        Modifier
            .fillMaxSize().background(Color.Red).clickable {
                viewModel.listf()
            }
    ) {

        val s =
            "content://com.android.externalstorage.documents/tree/primary%3AAB%2FTony%20Fadell%2FBuild%20An%20Unorthodox%20Guide%20to%20Making%20Things%20Worth%20Making"
        val x = URLDecoder.decode(s).substringAfter("primary:")

        Log.d("FILETEST",x)

    }
}