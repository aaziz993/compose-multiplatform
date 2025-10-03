package clib.presentation.components.navigation.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
public abstract class AbstractDestination {

    @Transient
    protected open val modifier: Modifier = Modifier

    @Transient
    protected open val selectedModifier: Modifier = modifier

    @Transient
    protected open val enabled: Boolean = true

    @Transient
    protected open val alwaysShowLabel: Boolean = true

    @Composable
    protected open fun Text(label: String, modifier: Modifier = Modifier): Unit =
        androidx.compose.material.Text(text = label)

    @Composable
    protected open fun SelectedText(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun Icon(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun SelectedIcon(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun Badge(label: String, modifier: Modifier = Modifier): Unit = Unit

    @Composable
    protected open fun SelectedBadge(label: String, modifier: Modifier = Modifier): Unit = Unit

    public abstract fun item(label: AbstractDestination.() -> String = { this::class.simpleName!! }): NavigationItem
}
