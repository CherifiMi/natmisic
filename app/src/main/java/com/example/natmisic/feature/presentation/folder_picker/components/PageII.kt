package com.example.natmisic.feature.presentation.folder_picker.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.natmisic.R

@Composable
fun PageII() {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.size(80.dp))
        Image(painter = painterResource(id = R.drawable.page2), contentDescription = "", modifier = Modifier
            .height(256.dp)
            .aspectRatio(1f)
        )
        Text(
            text = stringResource(id = R.string.page2_title),
            color = MaterialTheme.colors.secondary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 100.dp)
        )
        Text(
            text = stringResource(id = R.string.lorem),
            color = MaterialTheme.colors.secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 0.dp, horizontal = 70.dp),
            maxLines = 4
        )
    }
}