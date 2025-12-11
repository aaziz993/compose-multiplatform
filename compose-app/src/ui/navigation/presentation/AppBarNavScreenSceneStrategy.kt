package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import clib.presentation.theme.LocalThemeState
import data.type.primitives.string.asStringResource
import kotlin.collections.Map
import kotlinx.coroutines.launch

public class AppBarNavScreenSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = APP_BAR_NAV_SCREEN_KEY

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

            NavScreen(
                Modifier.fillMaxSize(),
                { Text(text = currentRoute.route.name.asStringResource(), overflow = TextOverflow.Clip, maxLines = 1) },
                connectivity,
                componentsState.components,
                themeState.theme,
                { value -> themeState.theme = value },
                config.localization.locales,
                localeState.localeInspectionAware(),
                { value -> localeState.locale = value },
                authState.auth,
                { value -> authState.auth = value },
                router.hasBack,
                layoutType == NavigationSuiteType.NavigationDrawer,
                navigationSuiteScaffoldState.currentValue == NavigationSuiteScaffoldValue.Visible,
                {
                    coroutineScope.launch {
                        navigationSuiteScaffoldState.toggle()
                    }
                },
                layoutType,
                navigationSuiteScaffoldState,
                router::actions,
                router.routes.items(
                    router = router,
                    auth = authState.auth,
                ),
                content = content,
            )
        }
    }

    public companion object Companion {

        private const val APP_BAR_NAV_SCREEN_KEY = "appBarNavScreen"

        public fun screen(): Map<String, Boolean> = mapOf(APP_BAR_NAV_SCREEN_KEY to true)
    }
}
