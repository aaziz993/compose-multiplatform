package data.type.primitives

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import compose_app.generated.resources.Res
import compose_app.generated.resources.disabled
import compose_app.generated.resources.enabled
import org.jetbrains.compose.resources.stringResource

@Composable
public fun Boolean.enabledStringResource(): String =
    if (this) stringResource(Res.string.enabled) else stringResource(Res.string.disabled)

@Suppress("ComposeModifierMissing")
@Composable
public fun Boolean.EnabledText(): Unit = Text(text = enabledStringResource())
