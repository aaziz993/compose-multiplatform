package ui.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
import presentation.components.scaffold.AppBar

public class AppBarSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = APP_BAR_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val config = LocalConfig.current
        val componentsState = LocalComponentsState.current
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val connectivity = LocalConnectivity.current
        val router = currentRouter()

        router.backStack.lastOrNull()?.let { currentRoute ->
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
                hasDrawer = false,
                isDrawerOpen = false,
                onNavigationAction = router::actions,
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    content()
                }
            }
        }
    }

    public companion object Companion {

        private const val APP_BAR_KEY = "appBar"

        public fun screen(): Map<String, Boolean> = mapOf(APP_BAR_KEY to true)
    }
}
