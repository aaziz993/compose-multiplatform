package presentation.components.scaffold

import androidx.compose.animation.core.EaseIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material.icons.outlined.Sos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.location.country.flag
import clib.data.type.primitives.string.asStringResource
import clib.presentation.auth.AuthComposable
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.components.picker.country.CountryPickerDialog
import clib.presentation.components.picker.country.mode.CountryPicker
import clib.presentation.easedVerticalGradient
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import compose_app.generated.resources.country_flag
import compose_app.generated.resources.language
import compose_app.generated.resources.menu
import compose_app.generated.resources.navigate_back
import compose_app.generated.resources.profile
import compose_app.generated.resources.search
import compose_app.generated.resources.sos
import compose_app.generated.resources.theme
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import klib.data.location.country.Country
import klib.data.location.country.getCountries
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.type.auth.model.Auth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.components.scaffold.model.ScreenAppBarMode
import presentation.components.tooltipbox.AppTooltipBox
import ui.navigation.presentation.Profile

@Composable
public fun AppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    blurEnabled: Boolean = HazeDefaults.blurEnabled(),
    mode: ScreenAppBarMode = ScreenAppBarMode.Default,
    inputScale: HazeInputScale = HazeInputScale.Default,
    theme: Theme = Theme(),
    onThemeChange: (Theme) -> Unit = {},
    locale: Locale? = null,
    onLocaleChange: (Locale?) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    hasDrawer: Boolean = true,
    isDrawerOpen: Boolean = true,
    onDrawerToggle: () -> Unit = {},
    hasBack: Boolean = true,
    onNavigationAction: (NavigationAction) -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit = {},
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
                title = title,
                navigationIcon = {
                    Row {
                        if (hasDrawer)
                            AppTooltipBox(stringResource(Res.string.menu)) {
                                IconButton(
                                    onClick = onDrawerToggle,
                                ) {
                                    Icon(
                                        imageVector = if (isDrawerOpen) Icons.Filled.Menu else Icons.Outlined.Menu,
                                        contentDescription = stringResource(Res.string.menu),
                                    )
                                }
                            }

                        if (hasBack)
                            AppTooltipBox(stringResource(Res.string.navigate_back)) {
                                IconButton(
                                    onClick = { onNavigationAction(NavigationAction.Pop) },
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
                    AuthComposable(auth) {
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
                    }

                    AppTooltipBox(stringResource(Res.string.theme)) {
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
                                contentDescription = stringResource(Res.string.theme),
                            )
                        }
                    }

                    var isCountryPickerDialogOpen by remember { mutableStateOf(false) }

                    if (isCountryPickerDialogOpen)
                        CountryPickerDialog(
                            onItemClicked = { country ->
                                country.locales().firstOrNull()?.let { locale ->
                                    onLocaleChange(locale)
                                    isCountryPickerDialogOpen = false
                                }
                            },
                            onDismissRequest = {
                                isCountryPickerDialogOpen = false
                            },
                            countries = Country.getCountries()
                                .filter { country -> country.locales().isNotEmpty() }
                                .toList()
                                .map { country ->
                                    country.copy(
                                        name = country.toString().asStringResource(Res.allStringResources) {
                                            country.name
                                        },
                                    )
                                },
                            picker = CountryPicker(
                                headerTitle = stringResource(Res.string.language),
                                searchHint = stringResource(Res.string.search),
                            ),
                        )

                    val country = (if (!LocalInspectionMode.current)
                        (locale ?: Locale.current).country()
                    else null) ?: Country.forCode("TJ")

                    AppTooltipBox(stringResource(Res.string.language)) {
                        Button(
                            onClick = {
                                isCountryPickerDialogOpen = true
                            },
                            modifier = Modifier.height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Unspecified,
                                disabledContainerColor = Color.Transparent,
                            ),
                            elevation = null,
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Image(
                                painter = painterResource(country.alpha2.flag),
                                contentDescription = stringResource(Res.string.country_flag),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .wrapContentWidth(),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }

                    AuthComposable(auth) { user ->
                        AppTooltipBox(stringResource(Res.string.profile)) {
                            IconButton(
                                onClick = {
                                    onNavigationAction(NavigationAction.Push(Profile))
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
        content = content,
    )
}

@Preview
@Composable
public fun PreviewAppBar(): Unit = AppBar()
