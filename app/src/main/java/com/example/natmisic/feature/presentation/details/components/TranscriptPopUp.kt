package com.example.natmisic.feature.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.natmisic.core.util.NoRippleInteractionSource
import com.example.natmisic.feature.domain.model.Timestamp
import com.example.natmisic.feature.presentation.details.DetailsEvent
import com.example.natmisic.feature.presentation.details.DetailsViewModel

@Composable
fun TranscriptPopUp(viewmodel: DetailsViewModel, item: Timestamp) {
    val LightBack = Color(0x4D7A70BB)
    val DarkBack = Color(0x4D000000)

    var txt by remember { mutableStateOf(item.text) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(if (!isSystemInDarkTheme()) LightBack else DarkBack)
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource()
            ) { viewmodel.onEvent(DetailsEvent.ClosePopup) }
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 70.dp)
                .aspectRatio(1.5f / 2f)
                .background(MaterialTheme.colors.primary, RoundedCornerShape(20.dp))
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.time,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(8.dp)
                )
                LazyColumn(modifier = Modifier.weight(4f)) {
                    item {
                        TextField(
                            value = txt, onValueChange = { txt = it }, modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.secondary,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            ),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.secondary,
                                backgroundColor = MaterialTheme.colors.primary,
                                cursorColor = MaterialTheme.colors.secondary,
                                focusedIndicatorColor = MaterialTheme.colors.primary,
                                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                                disabledIndicatorColor = MaterialTheme.colors.primary,
                            )
                        )
                    }
                }
                Row(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delete",
                        style = TextStyle(
                            color = MaterialTheme.colors.secondary,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = NoRippleInteractionSource()
                        ) {
                            viewmodel.onEvent(DetailsEvent.DeleteTimestamp(item))
                        }
                    )
                    Text(
                        text = if (txt == item.text)"Ok" else "Save",
                        style = TextStyle(
                            color = MaterialTheme.colors.secondary,
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = NoRippleInteractionSource()
                        ) {
                            if (txt != item.text){
                                viewmodel.onEvent(DetailsEvent.SaveTimestamp(item, txt))
                            }
                        }
                    )
                }
            }
        }
    }
}