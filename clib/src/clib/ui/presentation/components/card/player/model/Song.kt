package clib.ui.presentation.components.card.player.model

import kotlinx.serialization.Serializable

@Serializable
public data class Song(
    val title: String,
    val artist: String,
    val albumArtUrl: String,
    val duration: String,
    val currentTime: String,
    val progress: Float
)
