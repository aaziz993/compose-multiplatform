package clib.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import clib.presentation.event.alert.GlobalAlertEventController
import clib.presentation.event.alert.model.AlertEvent
import clib.presentation.navigation.exception.NavigationException
import clib.presentation.navigation.model.NavigationItem
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import kotlin.reflect.KClass
import kotlinx.coroutines.launch
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent

@Stable
@Immutable
public sealed class BaseRoute : Iterable<BaseRoute>, KoinComponent {

    @Suppress("UNCHECKED_CAST")
    public open val navRoute: KClass<out NavRoute>
        get() = requireNotNull(this::class as? KClass<out NavRoute>) { "No nav route" }
    public open val metadata: Map<String, Any> = emptyMap()

    public open val navigationItem: NavigationItem? = null
    public open val authResource: AuthResource? = null

    public fun isAuth(auth: Auth): Boolean =
        authResource?.validate(auth.provider, auth.user) != false

    context(scope: EntryProviderScope<NavRoute>)
    internal abstract fun entry(
        router: Router,
        auth: Auth,
        authRoute: NavRoute?,
        onBack: (() -> Unit)?,
        onError: ((NavigationException) -> Unit)? = null,
    )

    public open fun isNavigationItem(auth: Auth): Boolean = navigationItem != null && isAuth(auth)

    context(scope: NavigationSuiteScope)
    public open fun item(
        auth: Auth = Auth(),
        currentRoute: NavRoute,
        onNavigate: (NavRoute) -> Unit,
    ) {
        if (!isNavigationItem(auth)) return

        requireNotNull(this as? NavRoute) { "Not a route '$this'" }

        val selected = this == currentRoute
        val selectedItem = navigationItem!!.item(selected)

        scope.item(
            selected,
            { onNavigate(this) },
            selectedItem.icon,
            selectedItem.modifier,
            navigationItem!!.enabled,
            selectedItem.text,
            navigationItem!!.alwaysShowLabel,
            selectedItem.badge,
        )
    }

    @Suppress("ComposeParameterOrder")
    @Composable
    context(scope: RowScope)
    public open fun NavigationBarItem(
        auth: Auth = Auth(),
        currentRoute: NavRoute,
        onNavigate: (NavRoute) -> Unit,
    ) {
        if (!isNavigationItem(auth)) return

        requireNotNull(this as? NavRoute) { "Not a route '$this'" }

        val selected = this == currentRoute
        val selectedItem = navigationItem!!.item(selected)

        scope.NavigationBarItem(
            selected,
            { onNavigate(this) },
            selectedItem.icon,
            selectedItem.modifier,
            navigationItem!!.enabled,
            selectedItem.text,
            navigationItem!!.alwaysShowLabel,
        )
    }

    override fun toString(): String = navRoute.serializer().descriptor.serialName
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    @Composable
    protected abstract fun Content(route: T)

    @Suppress("UNCHECKED_CAST")
    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        router: Router,
        auth: Auth,
        authRoute: NavRoute?,
        onBack: (() -> Unit)?,
        onError: ((NavigationException) -> Unit)?,
    ): Unit = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) { navRoute ->
        Content(navRoute as T)
    }

    final override fun iterator(): Iterator<BaseRoute> = emptyList<BaseRoute>().iterator()
}

public abstract class Routes() : BaseRoute(), NavRoute {

    public abstract val routes: List<BaseRoute>

    public val startRoute: NavRoute by lazy {
        requireNotNull(routes.firstOrNull() as? NavRoute) { "No start route" }
    }

    override val metadata: Map<String, Any>
        get() = startRoute.route.metadata

    override val navigationItem: NavigationItem?
        get() = startRoute.route.navigationItem

    override val authResource: AuthResource?
        get() = startRoute.route.authResource

    @Composable
    protected open fun Content(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>,
    ): Unit = NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider,
    )

    @Composable
    public fun Content(
        router: Router = rememberRouter(),
        auth: Auth = Auth(),
        authRoute: NavRoute? = null,
        authRedirectRoute: NavRoute? = null,
        onBack: (() -> Unit)? = null,
        onError: ((NavigationException) -> Unit)? = null,
    ) {
        require(this.startRoute.route in routes) { "Start route '${this.startRoute.route}' isn't in '$routes'" }

        val coroutineScope = rememberCoroutineScope()
        val navigator = rememberNav3Navigator(
            this,
            auth,
            authRoute,
            authRedirectRoute,
            LocalParentRouter.current?.let { it::pop } ?: onBack ?: systemOnBack(),
            onError ?: { exception ->
                coroutineScope.launch {
                    GlobalAlertEventController.sendEvent(
                        AlertEvent(exception.message.orEmpty(), true),
                    )
                }
                Unit
            },
        )

        Nav3Host(
            router,
            navigator,
        ) {
            Content(
                navigator.backStack,
                onBack ?: LocalParentRouter.current?.let { it::pop } ?: systemOnBack(),
                entryProvider {
                    routes.forEach { route ->
                        route.entry(
                            router,
                            auth,
                            authRoute,
                            onBack,
                            onError,
                        )
                    }
                },
            )
        }
    }

    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        router: Router,
        auth: Auth,
        authRoute: NavRoute?,
        onBack: (() -> Unit)?,
        onError: ((NavigationException) -> Unit)?,
    ) = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) {
        Content(
            router = router,
            auth = auth,
            authRoute = authRoute,
            onBack = onBack,
            onError = onError,
        )
    }

    public fun isNavigationItems(auth: Auth): Boolean = routes.any { route -> route.isNavigationItem(auth) }

    context(scope: NavigationSuiteScope)
    public fun items(
        auth: Auth,
        currentRoute: NavRoute,
        onNavigate: (NavRoute) -> Unit,
    ): Unit = routes.forEach { route -> route.item(auth, currentRoute, onNavigate) }

    @Composable
    context(scope: RowScope)
    public fun NavigationBarItems(
        auth: Auth,
        currentRoute: NavRoute,
        onNavigate: (NavRoute) -> Unit,
    ): Unit = routes.forEach { route -> route.NavigationBarItem(auth, currentRoute, onNavigate) }

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()

    override fun toString(): String = startRoute.route.toString()
}

