package presentation.components.scaffold

import androidx.compose.animation.core.EaseIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material.icons.outlined.Sos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.auth.LocalAppAuth
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.navigation.LocalHasPreviousDestination
import clib.presentation.components.navigation.LocalTitle
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.easedVerticalGradient
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.theme.stateholder.ThemeAction
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.Profile
import ui.navigation.presentation.viewmodel.NavAction
import ui.navigation.presentation.viewmodel.NavState

public enum class ScaffoldMode {
    Default,
    Progressive,
    Mask,
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
public fun ScreenAppBar(
    onThemeAction: (ThemeAction) -> Unit,
    onAuthAction: (AuthAction) -> Unit,
    navState: NavState,
    onNavAction: (NavAction) -> Unit,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    blurEnabled: Boolean = HazeDefaults.blurEnabled(),
    mode: ScaffoldMode = ScaffoldMode.Default,
    inputScale: HazeInputScale = HazeInputScale.Default,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {

    val hazeState = rememberHazeState(blurEnabled = blurEnabled)

    val style = HazeMaterials.regular(MaterialTheme.colorScheme.surface)

    Scaffold(
        modifier = modifier
            .hazeEffect(state = hazeState, style = style) {
                this.inputScale = inputScale

                when (mode) {
                    ScaffoldMode.Default -> Unit
                    ScaffoldMode.Progressive ->
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                        )

                    ScaffoldMode.Mask -> mask = Brush.easedVerticalGradient(EaseIn)
                }
            },
        topBar = {
            TopAppBar(
                title = { Text(LocalTitle.current) },
                navigationIcon = {
                    Row {
                        if (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                            AppTooltipBox("Menu") {
                                IconButton(
                                    onClick = {
                                        onNavAction(NavAction.ToggleDrawer)
                                    },
                                ) {
                                    Icon(
                                        imageVector = if (navState.isDrawerOpen) Icons.Filled.Menu else Icons.Outlined.Menu,
                                        contentDescription = "Menu",
                                    )
                                }
                            }


                        if (LocalHasPreviousDestination.current)
                            AppTooltipBox("Navigate back") {
                                IconButton(
                                    onClick = { onNavigationAction(NavigationAction.NavigateBack) },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Navigate back",
                                    )
                                }
                            }
                    }
                },
                actions = {
                    AppTooltipBox("SOS") {
                        IconButton(
                            onClick = {

                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Sos,
                                contentDescription = "SOS",
                            )
                        }
                    }

                    val theme = LocalAppTheme.current
                    AppTooltipBox("Switch theme") {
                        IconButton(
                            onClick = {
                                when (theme.mode) {
                                    ThemeMode.SYSTEM -> onThemeAction(ThemeAction.SetTheme(theme.copy(mode = ThemeMode.LIGHT)))
                                    ThemeMode.LIGHT -> onThemeAction(ThemeAction.SetTheme(theme.copy(mode = ThemeMode.DARK)))
                                    ThemeMode.DARK -> onThemeAction(ThemeAction.SetTheme(theme.copy(mode = ThemeMode.SYSTEM)))
                                }
                            },
                        ) {
                            Icon(
                                imageVector = when (theme.mode) {
                                    ThemeMode.SYSTEM -> Icons.Outlined.SettingsBrightness
                                    ThemeMode.LIGHT -> Icons.Outlined.LightMode
                                    ThemeMode.DARK -> Icons.Outlined.DarkMode
                                },
                                contentDescription = "Switch theme",
                            )
                        }
                    }
                    AppTooltipBox("Profile") {
                        val user = LocalAppAuth.current.user!!
                        Avatar(
                            user = user,
                            modifier = Modifier
                                .height(TopAppBarDefaults.TopAppBarExpandedHeight)
                                .aspectRatio(1f)
                                .padding(end = 20.dp)
                                .clickable {
                                    onNavigationAction(NavigationAction.TypeNavigation.Navigate(Profile))
                                },
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
            )
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}
