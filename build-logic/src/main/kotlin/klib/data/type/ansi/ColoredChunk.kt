package klib.data.type.ansi

import kotlinx.serialization.Serializable

@Serializable
public data class ColoredChunk(
    val text: String,
    val fgHex: String? = null,
    val bgHex: String? = null,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
)
