package presentation.components.scaffold

import androidx.compose.animation.core.EaseIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.window.core.layout.WindowWidthSizeClass
import clib.data.location.country.flag
import clib.presentation.auth.LocalAuth
import clib.presentation.auth.stateholder.AuthAction
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.navigation.LocalDestinationTitle
import clib.presentation.components.navigation.LocalHasPreviousDestination
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.components.picker.country.CountryPickerDialog
import clib.presentation.easedVerticalGradient
import clib.presentation.locale.LocalAppLocale
import clib.presentation.locale.stateholder.LocaleAction
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.theme.stateholder.ThemeAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import compose_app.generated.resources.country_flag
import compose_app.generated.resources.language
import compose_app.generated.resources.menu
import compose_app.generated.resources.navigate_back
import compose_app.generated.resources.profile
import compose_app.generated.resources.sos
import compose_app.generated.resources.theme
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import klib.data.location.country.Country
import klib.data.location.locale.Locale
import klib.data.location.locale.setCurrent
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import presentation.components.scaffold.model.ScreenAppBarMode
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.AuthRoute
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Profile

@Composable
public fun ScreenAppBar(
    onThemeAction: (ThemeAction) -> Unit,
    onLocaleAction: (LocaleAction) -> Unit,
    onAuthAction: (AuthAction) -> Unit,
    isDrawerOpen: Boolean,
    toggleDrawer: () -> Unit,
    onNavigationAction: (NavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    blurEnabled: Boolean = HazeDefaults.blurEnabled(),
    mode: ScreenAppBarMode = ScreenAppBarMode.Default,
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
                    ScreenAppBarMode.Default -> Unit
                    ScreenAppBarMode.Progressive ->
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                        )

                    ScreenAppBarMode.Mask -> mask = Brush.easedVerticalGradient(EaseIn)
                }
            },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Res.allStringResources[LocalDestinationTitle.current]?.let { stringResource ->
                            stringResource(stringResource)
                        } ?: LocalDestinationTitle.current,
                    )
                },
                navigationIcon = {
                    Row {
                        val hasNavigationItems = NavRoute.filterNot(AuthRoute::contains).toList().any { route ->
                            route.canNavigateItem(LocalAuth.current)
                        }
                        if (hasNavigationItems && currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                            AppTooltipBox(stringResource(Res.string.menu)) {
                                IconButton(
                                    onClick = toggleDrawer,
                                ) {
                                    Icon(
                                        imageVector = if (isDrawerOpen) Icons.Filled.Menu else Icons.Outlined.Menu,
                                        contentDescription = stringResource(Res.string.menu),
                                    )
                                }
                            }

                        if (LocalHasPreviousDestination.current)
                            AppTooltipBox(stringResource(Res.string.navigate_back)) {
                                IconButton(
                                    onClick = { onNavigationAction(NavigationAction.NavigateBack) },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(Res.string.navigate_back),
                                    )
                                }
                            }
                    }
                },
                actions = {
                    if (LocalAuth.current.user != null)
                        AppTooltipBox(stringResource(Res.string.sos)) {
                            IconButton(
                                onClick = {

                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Sos,
                                    contentDescription = stringResource(Res.string.sos),
                                )
                            }
                        }

                    val theme = LocalAppTheme.current

                    AppTooltipBox(stringResource(Res.string.theme)) {
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
                                contentDescription = stringResource(Res.string.theme),
                            )
                        }
                    }

                    var isCountryPickerDialogOpen by remember { mutableStateOf(false) }

                    if (isCountryPickerDialogOpen)
                        CountryPickerDialog(
                            onItemClicked = { country ->
                                country.locales().firstOrNull()?.let { locale ->
                                    onLocaleAction(LocaleAction.SetLocale(locale))
                                    isCountryPickerDialogOpen = false
                                }
                            },
                            onDismissRequest = {
                                isCountryPickerDialogOpen = false
                            },
                        )

                    val country = (if (!LocalInspectionMode.current)
                        LocalAppLocale.current.countries().firstOrNull()
                    else null) ?: Country.forCode("TJ")

                    AppTooltipBox(stringResource(Res.string.language)) {
                        IconButton(
                            onClick = {
                                isCountryPickerDialogOpen = true
                            },
                            modifier = Modifier
                                .clip(RectangleShape),
                        ) {
                            Image(
                                bitmap = imageResource(country.alpha2.flag),
                                contentDescription = stringResource(Res.string.country_flag),
                            )
                        }
                    }

                    AppTooltipBox(stringResource(Res.string.profile)) {
                        LocalAuth.current.user?.let { user ->
                            IconButton(
                                onClick = {
                                    onNavigationAction(NavigationAction.TypeNavigation.Navigate(Profile))
                                },
                            ) {
                                Avatar(
                                    user = user,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                )
                            }
                        }
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
