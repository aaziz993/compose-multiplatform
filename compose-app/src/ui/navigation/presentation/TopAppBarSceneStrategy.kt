package ui.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import clib.presentation.appbar.LocalAppBarState
import clib.presentation.auth.LocalAuthState
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivityState
import clib.presentation.connectivity.LocalConnectivityStatus
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.inspectionModeAware
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.WrapperSceneStrategy
import clib.presentation.theme.LocalThemeState
import data.type.primitives.string.asStringResource
import kotlin.collections.Map
import presentation.components.scaffold.TopAppBar

public class TopAppBarSceneStrategy : WrapperSceneStrategy<NavRoute>() {

    override val key: String = TOP_APP_BAR_KEY

    @Composable
    override fun Content(content: @Composable () -> Unit) {
        val config = LocalConfig.current
        val connectivityStatus = LocalConnectivityStatus.current
        val appBarState = LocalAppBarState.current
        val connectivityState = LocalConnectivityState.current
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val router = currentRouter()

        router.backStack.lastOrNull()?.let { currentRoute ->
            TopAppBar(
                modifier = Modifier.fillMaxSize(),
                title = { Text(text = currentRoute.route.name.asStringResource(), overflow = TextOverflow.Clip, maxLines = 1) },
                connectivityStatus = connectivityStatus,
                appBar = appBarState.value,
                connectivity = connectivityState.value,
                theme = themeState.value,
                onThemeChange = { value -> themeState.value = value },
                locales = config.localization.locales,
                locale = localeState.value.inspectionModeAware,
                onLocaleChange = { value -> localeState.value = value },
                auth = authState.value,
                onAuthChange = { value -> authState.value = value },
                hasBack = router.canPopBack,
                hasDrawer = false,
                isDrawerOpen = false,
                onNavigationActions = router::actions,
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    content()
                }
            }
        }
    }

    public companion object Companion {

        private const val TOP_APP_BAR_KEY = "topAppBar"

        public fun screen(): Map<String, Boolean> = mapOf(TOP_APP_BAR_KEY to true)
    }
}
