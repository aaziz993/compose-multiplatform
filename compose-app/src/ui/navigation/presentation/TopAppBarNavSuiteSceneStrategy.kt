package ui.navigation.presentation

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
import clib.presentation.appbar.LocalAppBarState
import clib.presentation.auth.LocalAuthState
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivityState
import clib.presentation.connectivity.LocalConnectivityStatus
import clib.presentation.locale.LocalLocaleState
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import clib.presentation.theme.LocalThemeState
import data.type.primitives.string.asStringResource
import kotlin.collections.Map
import kotlinx.coroutines.launch
import presentation.components.scaffold.TopAppBar

public class TopAppBarNavSuiteSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = TOP_APP_BAR_NAV_SUITE_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val config = LocalConfig.current
        val appBarState = LocalAppBarState.current
        val connectivityState = LocalConnectivityState.current
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val connectivityStatus = LocalConnectivityStatus.current
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

            TopAppBar(
                modifier = Modifier.fillMaxSize(),
                title = { Text(text = currentRoute.route.name.asStringResource(), overflow = TextOverflow.Clip, maxLines = 1) },
                connectivityStatus = connectivityStatus,
                appBar = appBarState.value,
                connectivity = connectivityState.value,
                theme = themeState.value,
                onThemeChange = { value -> themeState.value = value },
                locales = config.localization.locales,
                locale = localeState.localeInspectionAware(),
                onLocaleChange = { value -> localeState.value = value },
                auth = authState.value,
                onAuthChange = { value -> authState.value = value },
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
                NavigationSuiteScaffold(
                    navigationSuiteItems = router.routes.items(
                        alwaysShowLabel = if (layoutType == NavigationSuiteType.NavigationDrawer) {
                            { true }
                        }
                        else BaseRoute::alwaysShowLabel,
                        auth = authState.value,
                    ),
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    layoutType = layoutType,
                    state = navigationSuiteScaffoldState,
                    content = content,
                )
            }
        }
    }

    public companion object Companion {

        private const val TOP_APP_BAR_NAV_SUITE_KEY = "topAppBarNavSuite"

        public fun screen(): Map<String, Boolean> = mapOf(TOP_APP_BAR_NAV_SUITE_KEY to true)
    }
}
