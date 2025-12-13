package ui.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.window.core.layout.WindowSizeClass
import clib.presentation.auth.LocalAuthState
import clib.presentation.components.LocalComponentsState
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivity
import clib.presentation.locale.LocalLocaleState
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import clib.presentation.theme.LocalThemeState
import data.type.primitives.string.asStringResource
import kotlin.collections.Map
import kotlinx.coroutines.launch
import presentation.components.scaffold.AppBar

public class NavSuiteAppBarSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = NAV_SUITE_APP_BAR_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val config = LocalConfig.current
        val componentsState = LocalComponentsState.current
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val connectivity = LocalConnectivity.current
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

            NavigationSuiteScaffold(
                navigationSuiteItems = router.routes.items(
                    router = router,
                    alwaysShowLabel = if (layoutType == NavigationSuiteType.NavigationDrawer) {
                        { true }
                    }
                    else BaseRoute::enabled,
                    auth = authState.auth,
                ),
                modifier = Modifier.fillMaxSize(),
                layoutType = layoutType,
                state = navigationSuiteScaffoldState,
            ) {
                AppBar(
                    modifier = Modifier.fillMaxSize(),
                    title = { Text(text = currentRoute.route.name.asStringResource(), overflow = TextOverflow.Clip, maxLines = 1) },
                    components = componentsState.components,
                    theme = themeState.theme,
                    onThemeChange = { value -> themeState.theme = value },
                    locales = config.localization.locales,
                    locale = localeState.localeInspectionAware(),
                    onLocaleChange = { value -> localeState.locale = value },
                    auth = authState.auth,
                    onAuthChange = { value -> authState.auth = value },
                    connectivity = connectivity,
                    hasBack = router.hasBack,
                    hasDrawer = layoutType == NavigationSuiteType.NavigationDrawer,
                    isDrawerOpen = navigationSuiteScaffoldState.currentValue == NavigationSuiteScaffoldValue.Visible,
                    onDrawerToggle = {
                        coroutineScope.launch {
                            navigationSuiteScaffoldState.toggle()
                        }
                    },
                    onNavigationActions = router::actions,
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        content()
                    }
                }
            }
        }
    }

    public companion object Companion {

        private const val NAV_SUITE_APP_BAR_KEY = "navSuiteAppBar"

        public fun screen(): Map<String, Boolean> = mapOf(NAV_SUITE_APP_BAR_KEY to true)
    }
}
