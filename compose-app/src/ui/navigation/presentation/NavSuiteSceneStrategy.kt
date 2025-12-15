package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import clib.presentation.auth.LocalAuthState
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import kotlin.collections.Map
import kotlinx.coroutines.launch

public class NavSuiteSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = NAV_SUITE_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val authState = LocalAuthState.current
        val router = currentRouter()
        val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        router.backStack.lastOrNull()?.let { currentRoute ->
            val layoutType = if (router.routes.isNavigationItems(authState.value)) {
                val adaptiveInfo = currentWindowAdaptiveInfo()
                with(adaptiveInfo) {
                    if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                        NavigationSuiteType.NavigationDrawer
                    else {
                        if (navigationSuiteScaffoldState.currentValue == NavigationSuiteScaffoldValue.Hidden)
                            coroutineScope.launch {
                                navigationSuiteScaffoldState.show()
                            }
                        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(this)
                    }
                }
            }
            else NavigationSuiteType.None

            NavigationSuiteScaffold(
                navigationSuiteItems = router.routes.items(
                    alwaysShowLabel = if (layoutType == NavigationSuiteType.NavigationDrawer) {
                        { true }
                    }
                    else BaseRoute::alwaysShowLabel,
                    auth = authState.value,
                ),
                modifier = Modifier.fillMaxSize(),
                layoutType = layoutType,
                state = navigationSuiteScaffoldState,
                content = content,
            )
        }
    }

    public companion object Companion {

        private const val NAV_SUITE_KEY = "navSuite"

        public fun screen(): Map<String, Boolean> = mapOf(NAV_SUITE_KEY to true)
    }
}
