package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import clib.presentation.auth.LocalAuthState
import clib.presentation.config.LocalConfig
import clib.presentation.locale.LocalLocaleState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.state.LocalStateStore
import clib.presentation.theme.LocalThemeState
import data.type.primitives.string.asStringResource
import kotlin.collections.Map

public class NavScreenSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = NAV_SCREEN_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val config = LocalConfig.current
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val stateStore = LocalStateStore.current
        val router = currentRouter()
        router.backStack.lastOrNull()?.let { currentRoute ->
            val layoutType = if (router.routes.isNavigationItems(authState.auth)) {
                val adaptiveInfo = currentWindowAdaptiveInfo()
                with(adaptiveInfo) {
                    if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                        NavigationSuiteType.NavigationDrawer
                    else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(this)
                }
            }
            else NavigationSuiteType.None

            NavScreen(
                Modifier.fillMaxSize(),
                { Text(text = currentRoute.route.name.asStringResource()) },
                themeState.theme,
                { value -> themeState.theme = value },
                config.localization.locales,
                localeState.localeInspectionAware(),
                { value -> localeState.locale = value },
                authState.auth,
                { value -> authState.auth = value },
                stateStore.get<QuickAccess>(),
                layoutType == NavigationSuiteType.NavigationDrawer,
                router.hasBack,
                layoutType,
                router::actions,
                router.routes.items(
                    router = router,
                    auth = authState.auth,
                ),
                content = content,
            )
        }
    }

    public companion object {

        private const val NAV_SCREEN_KEY = "navScreen"

        public fun navScreen(): Map<String, Boolean> = mapOf(NAV_SCREEN_KEY to true)
    }
}
