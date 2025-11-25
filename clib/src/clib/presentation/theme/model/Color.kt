package clib.presentation.theme.model

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class Color(public val value: ULong) {

    public fun toColor(): androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(value)
}

public fun androidx.compose.ui.graphics.Color.toColor(): Color = Color(value)

@Composable
public fun color(error: Boolean): androidx.compose.ui.graphics.Color =
    if (error) MaterialTheme.colorScheme.error else LocalContentColor.current
