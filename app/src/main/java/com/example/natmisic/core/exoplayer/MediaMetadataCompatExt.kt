package com.example.natmisic.core.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.natmisic.feature.domain.model.Book

fun MediaMetadataCompat.toBook(): Book? {
    return description?.let {
        Book(
            id =it.mediaId!!.toInt(),
            path =it.mediaUri.toString(),
            name =it.title.toString(),
            author =it.subtitle.toString(),
            cover =it.iconUri.toString(),
            duration =0,
            progress =0,
            timestamp = emptyList(),
        )
    }
}