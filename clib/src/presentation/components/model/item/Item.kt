package presentation.components.model.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public open class Item(
    public val text: (@Composable (Modifier) -> Unit)? = null,
    public val icon: (@Composable (Modifier) -> Unit)? = null,
    public val badge: (@Composable (Modifier) -> Unit)? = null,
    public val modifier: Modifier = Modifier,
)
