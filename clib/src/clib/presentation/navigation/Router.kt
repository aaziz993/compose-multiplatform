package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.http.Url
import klib.data.type.collections.linkedlist.model.Node
import klib.data.type.collections.list.drop

/**
 * CompositionLocal that provides access to the parent Router in nested navigation hierarchies.
 *
 * This is used to establish a parent-child relationship between navigation containers.
 * When a nested Nav3Host is created, it can access its parent router through this CompositionLocal
 * to properly handle navigation events like pop() and dropStack().
 *
 * Value is null for root navigation containers (no parent exists).
 * Value is non-null for nested navigation containers (parent router available).
 *
 * Users typically don't need to access this directly - it's managed automatically by Nav3Host.
 */
@Suppress("ComposeCompositionLocalUsage")
internal val LocalRouter: ProvidableCompositionLocal<Router?> = compositionLocalOf { null }

/**
 * Main router implementation providing high-level navigation operations.
 *
 * Router offers a convenient API for common navigation patterns like push, pop, replace,
 * and stack manipulation. It translates these operations into appropriate Command objects
 * and executes them through the action queue system.
 *
 * This class is designed to be used as the primary navigation interface in applications.
 *
 * @param routes The current top level route.
 */
public open class Router(public val routes: Routes) : BaseRouter(), Node<Router> {

    /**
     * Parent router in nested navigation hierarchy.
     */
    override var prev: Router? by mutableStateOf(null)
        private set

    /**
     * Child router in nested navigation hierarchy.
     */
    override var next: Router? by mutableStateOf(null)
        private set

    /**
     * Currently registered navigator's can pop back.
     */
    public val canPopBack: Boolean
        get() = backStack.size > 1 || prev?.canPopBack == true

    /**
     * Queue of deep route segments that are pending to be navigated.
     *
     * This path is processed one by one when using `routeDeep`.
     * It is automatically cleared if any new navigation action is executed.
     */
    public var pendingRoutePath: List<NavRoute> = emptyList()
        protected set

    /**
     * Binds the parent router.
     */
    internal fun bindParent(prev: Router) {
        require(this != prev) { "Router can't be parent of itself" }

        prev.next = this
        this.prev = prev
        pendingRoutePath = prev.pendingRoutePath.drop()
    }

    /**
     * Unbinds the parent router.
     */
    internal fun unbindParent(prev: Router) {
        if (prev.next === this) prev.next = null

        this.prev = null
        pendingRoutePath = emptyList()
    }

    /**
     * Handles the initial phase of deep routing for a resolved route path.
     *
     * Executes the first actionable route in the path (skipping the root route),
     * and stores the remaining segments in [pendingRoutePath] for subsequent navigation.
     */
    protected fun handleRoutePath(navRoutePath: List<NavRoute>, action: Router.(NavRoute) -> Unit) {
        action(navRoutePath[1])
        this.pendingRoutePath = navRoutePath.drop(2)
    }

    public fun routeTo(navRoute: NavRoute, action: Router.(NavRoute) -> Unit = Router::push) {
        routes.resolvePathTo(navRoute)?.let { navRoute -> handleRoutePath(navRoute, action) }
            ?: checkNotNull(prev) { "Unknown route '$navRoute'" }.routeTo(navRoute, action)
    }

    public fun routeTo(url: Url, action: Router.(NavRoute) -> Unit = Router::push) {
        routes.resolvePathTo(url)?.let { navRoute -> handleRoutePath(navRoute, action) }
            ?: checkNotNull(prev) { "Unknown route '$url'" }.routeTo(url, action)
    }

    /**
     * Pushes one or more routes onto the navigation stack.
     *
     * Each route will be added to the top of the stack in the order provided.
     * This allows for building deep navigation chains in a single operation.
     *
     * @param routes Variable number of routes to push onto the stack.
     * @throws IllegalArgumentException if no routes are provided.
     */
    public fun push(vararg routes: NavRoute) {
        require(routes.isNotEmpty()) { "Screens must not be empty" }

        val actions = routes.map(NavigationAction::Push).toTypedArray()
        actions(*actions)
    }

    /**
     * Replaces the current top route with a new route.
     *
     * If the stack is empty, the new route will be added as the first route.
     * This is useful for scenarios like login flow completion or error recovery.
     *
     * @param route The route to replace the current top route with.
     */
    public fun replaceCurrent(route: NavRoute): Unit =
        actions(NavigationAction.ReplaceCurrent(route))

    /**
     * Replaces the entire navigation stack with new routes.
     *
     * Useful for major navigation flow changes like switching between authenticated/unauthenticated states.
     *
     * @param routes Variable number of routes to replace the stack with.
     * @triggers system back navigation when the stack is empty.
     */
    public fun replaceStack(vararg routes: NavRoute): Unit =
        actions(NavigationAction.ReplaceStack(routes.toList()))

    /**
     * Removes the top route from the navigation stack.
     *
     * If only one route remains, this will trigger system back navigation.
     * (typically exiting the app or returning to the previous activity).
     */
    public fun pop(): Unit = actions(NavigationAction.Pop)

    /**
     * Navigates back to a specific route in the stack.
     *
     * Removes all routes above the target route. If the target route is not found,
     * all routes except the root will be removed.
     *
     * @param route The target route to navigate back to.
     */
    public fun popTo(route: NavRoute): Unit = actions(NavigationAction.PopTo(route))

    /**
     * Clears all routes except the root route.
     *
     * This resets the navigation stack to its initial state while preserving the root.
     * Useful for "back to main" functionality or clearing complex navigation flows.
     */
    public fun resetToRoot(): Unit = actions(NavigationAction.ResetToRoot)

    /**
     * Removes all routes except the current top route.
     *
     * The current route becomes the new root of the stack and
     * system back navigation will be triggered.
     *
     * Use it when you want to close a nested navigation graph or a whole application.
     */
    public fun dropStack(): Unit = actions(NavigationAction.DropStack)

    final override fun actions(vararg actions: NavigationAction) {
        pendingRoutePath = emptyList()
        super.actions(*actions)
    }
}

@Composable
public fun currentRouter(): Router =
    checkNotNull(LocalRouter.current) { "No Router was provided via LocalRouter" }
