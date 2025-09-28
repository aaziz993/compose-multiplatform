package presentation

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class Color(public val value: Long) {

    public fun toColor(): Color = Color(value)
}

@Composable
public fun color(error: Boolean): Color = if (error) MaterialTheme.colorScheme.error else LocalContentColor.current
