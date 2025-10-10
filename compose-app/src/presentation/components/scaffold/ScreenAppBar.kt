package presentation.components.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.auth.LocalAppAuth
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.navigation.LocalBackButton
import clib.presentation.components.navigation.LocalTitle
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.theme.stateholder.ThemeAction
import klib.data.type.auth.model.Auth
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.Profile

@Composable
public fun ScreenAppBar(
    theme: Theme,
    themeAction: (ThemeAction) -> Unit,
    auth: Auth,
    authAction: (AuthAction) -> Unit,
    isOpenDrawer: Boolean,
    onOpenDrawerChange: (Boolean) -> Unit,
    navigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(LocalTitle.current) },
                navigationIcon = {
                    Row {
                        if (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                            AppTooltipBox("Menu") {
                                IconButton(
                                    onClick = {
                                        onOpenDrawerChange(!isOpenDrawer)
                                    },
                                ) {
                                    Icon(
                                        imageVector = if (isOpenDrawer) Icons.Filled.Menu else Icons.Outlined.Menu,
                                        contentDescription = "Menu",
                                    )
                                }
                            }


                        if (LocalBackButton.current)
                            AppTooltipBox("Navigate back") {
                                IconButton(
                                    onClick = { navigationAction(NavigationAction.NavigateBack) },
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
                    AppTooltipBox("Switch theme") {
                        IconButton(
                            onClick = {
                                when (theme.mode) {
                                    ThemeMode.SYSTEM -> onThemeChange(theme.copy(mode = ThemeMode.LIGHT))
                                    ThemeMode.LIGHT -> onThemeChange(theme.copy(mode = ThemeMode.DARK))
                                    ThemeMode.DARK -> onThemeChange(theme.copy(mode = ThemeMode.SYSTEM))
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
                                .padding(end = 8.dp)
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .clickable {
                                    navigationAction(NavigationAction.TypeSafeNavigation.Navigate(Profile))
                                },
                        )
                    }
                },
            )
        },
    ) {
        content()
    }
}
