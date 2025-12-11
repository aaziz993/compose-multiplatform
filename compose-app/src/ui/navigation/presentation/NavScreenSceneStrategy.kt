package ui.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import clib.presentation.auth.LocalAuthState
import clib.presentation.event.alert.GlobalAlertDialog
import clib.presentation.event.snackbar.GlobalSnackbar
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import kotlin.collections.Map
import kotlinx.coroutines.launch

public class NavScreenSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = NAV_SCREEN_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val authState = LocalAuthState.current
        val router = currentRouter()
        val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
        val coroutineScope = rememberCoroutineScope()

        router.backStack.lastOrNull()?.let { currentRoute ->
            val layoutType = if (router.routes.isNavigationItems(authState.auth)) {
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

            Box(modifier = Modifier.fillMaxSize()) {
                NavigationSuiteScaffold(
                    navigationSuiteItems = router.routes.items(
                        router = router,
                        alwaysShowLabel = if (layoutType == NavigationSuiteType.NavigationDrawer) {
                            { true }
                        }
                        else BaseRoute::enabled,
                        auth = authState.auth,
                    ),
                    layoutType = layoutType,
                    state = navigationSuiteScaffoldState,
                    content = content,
                )

                GlobalAlertDialog()
                GlobalSnackbar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    public companion object Companion {

        private const val NAV_SCREEN_KEY = "navScreen"

        public fun screen(): Map<String, Boolean> = mapOf(NAV_SCREEN_KEY to true)
    }
}
