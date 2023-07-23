package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.natmisic.R
import java.io.File

@Composable
fun CoverTab(cover: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .padding(32.dp)
                .aspectRatio(1f)
                .background(MaterialTheme.colors.primary, RoundedCornerShape(10))
                .clip(RoundedCornerShape(10)),
            painter = if (cover != "") rememberAsyncImagePainter(model = File(cover)) else painterResource(R.drawable.defult_cover),
            contentDescription = "",
        )
    }
}