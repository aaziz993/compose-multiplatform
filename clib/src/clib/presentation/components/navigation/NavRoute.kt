package clib.presentation.components.navigation

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import clib.presentation.components.model.item.Item
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
@Immutable
public abstract class NavRoute<T : Route> {

    protected open val route: T?
        get() = this as? T

    protected open val enabled: Boolean
        get() = true
    protected open val alwaysShowLabel: Boolean
        get() = true

    protected open val modifier: Modifier
        get() = Modifier
    protected open val selectedModifier: Modifier
        get() = modifier
    protected open val text: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { text, modifier -> Text(text = text, modifier = modifier) }
    protected open val selectedText: @Composable (label: String, modifier: Modifier) -> Unit
        get() = text
    protected open val icon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val selectedIcon: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val badge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }
    protected open val selectedBadge: @Composable (label: String, modifier: Modifier) -> Unit
        get() = { _, _ -> }

    @Composable
    public open fun ParentContent(content: @Composable () -> Unit): Unit = content()

    @Composable
    protected open fun Content(route: T): Unit = Unit

    public fun entry(
        route: T,
        metadata: Map<String, Any> = emptyMap(),
    ): NavEntry<T> = NavEntry(
        route,
        route.name,
        metadata,
    ) { key -> Content(key) }

    public open fun authResource(): AuthResource? = null

    context(navigationSuiteScope: NavigationSuiteScope)
    public open fun item(
        auth: Auth = Auth(),
        currentRoute: T,
        transform: @Composable (name: String) -> String = { it.uppercaseFirstChar() },
        navigateTo: (T) -> Unit,
    ): Unit = with(navigationSuiteScope) {
        if (!isNavigateItem(auth)) return@with

        val name = route!!.name
        val selected = route == currentRoute

        val selectedItem = if (selected)
            Item(
                selectedModifier,
                { selectedText(transform(name), it) },
                { selectedIcon(transform(name), it) },
                { selectedBadge(transform(name), it) },
            )
        else Item(
            modifier,
            { text(transform(name), it) },
            { icon(transform(name), it) },
            { badge(transform(name), it) },
        )

        item(
            selected,
            { navigateTo(route!!) },
            { selectedItem.icon?.invoke(Modifier) },
            selectedItem.modifier,
            enabled,
            { selectedItem.text?.invoke(Modifier) },
            alwaysShowLabel,
            { selectedItem.badge?.invoke(Modifier) },
        )
    }

    internal fun isNavigateItem(auth: Auth): Boolean = route != null && isAuth(auth)

    public fun isAuth(auth: Auth): Boolean =
        authResource()?.validate(auth.provider, auth.user) != false
}
