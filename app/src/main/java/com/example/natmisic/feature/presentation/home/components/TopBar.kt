package com.example.natmisic.feature.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.natmisic.R
import com.example.natmisic.feature.presentation.util.Screens

@Composable
fun TopBar(navController: NavHostController) {
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.search_ic),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
        IconButton(onClick = { navController.navigate(Screens.SETTINGS.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.settings_ic),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}