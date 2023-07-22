package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.natmisic.feature.domain.model.Timestamp

@Composable
fun TranscriptTab(timestamp: List<Timestamp?>) {
    Box(modifier = Modifier.fillMaxSize()){
        // items
        LazyColumn(Modifier.fillMaxSize(), reverseLayout = true) {
            item {
                Spacer(modifier = Modifier.size(200.dp))
            }
            items(timestamp){
                Text(text = it!!.text)
                Text(text = it!!.time)
            }
            item {
                Spacer(modifier = Modifier.size(200.dp))
            }
        }
        // fading


        // popup


    }
}