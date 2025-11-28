package data.type.primitives

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import compose_app.generated.resources.Res
import compose_app.generated.resources.disabled
import compose_app.generated.resources.enabled
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeModifierMissing")
@Composable
public fun Boolean.EnabledText(): Unit = if (this) Text(text = stringResource(Res.string.enabled))
else Text(text = stringResource(Res.string.disabled))
