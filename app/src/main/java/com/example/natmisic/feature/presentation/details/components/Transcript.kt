package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.natmisic.feature.domain.model.Timestamp

@Composable
fun TranscriptTab(timestamp: List<Timestamp?>) {
    Box(modifier = Modifier.fillMaxSize()) {
        // items
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 60.dp), reverseLayout = true) {
            item {
                Spacer(modifier = Modifier.size(200.dp))
            }
            items(timestamp) {
                Text(
                    lineHeight = 20.sp,
                    text = it!!.text,
                    color = MaterialTheme.colors.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.size(200.dp))
            }
        }
        Box(modifier = Modifier.fillMaxSize()){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Brush.verticalGradient(listOf(MaterialTheme.colors.primary, Color.Transparent)))
                    .align(Alignment.TopCenter)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colors.primary)))
                    .align(Alignment.BottomCenter)
            )
        }
    }
}