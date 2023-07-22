package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.natmisic.feature.domain.model.Timestamp

@Composable
fun TranscriptTab(timestamp: List<Timestamp?>) {
    Box(modifier = Modifier.fillMaxSize()){
        // items
        LazyColumn(Modifier.fillMaxSize(), reverseLayout = true) {
            items(timestamp){
                Text(text = it!!.text)
                Text(text = it!!.time)
            }
        }
        // fading


        // popup


    }
}