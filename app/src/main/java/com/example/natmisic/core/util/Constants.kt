package com.example.natmisic.core.util

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object Constants {

    const val SERVICE_TAG = "MusicService"

    const val MEDIA_ROOT_ID = "root_id"

    const val NETWORK_FAILURE = "NETWORK_FAILURE"

    const val UPDATE_PLAYER_POSITION_INTERVAL = 100L

    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "music"
}

class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}