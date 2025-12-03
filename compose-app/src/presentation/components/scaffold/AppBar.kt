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
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.location.country.flag
import clib.presentation.auth.AuthComposable
import clib.presentation.components.country.LocalePickerDialog
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.easedVerticalGradient
import clib.presentation.navigation.NavigationAction
import clib.presentation.quickaccess.QuickAccess
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.back
import compose_app.generated.resources.country_flag
import compose_app.generated.resources.help
import compose_app.generated.resources.language
import compose_app.generated.resources.menu
import compose_app.generated.resources.profile
import compose_app.generated.resources.search
import compose_app.generated.resources.theme
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.type.auth.model.Auth
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.components.scaffold.model.ScreenAppBarMode
import presentation.components.tooltipbox.AppTooltipBox
import presentation.theme.model.IsDarkIcon
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
    locales: List<Locale> = emptyList(),
    locale: Locale = Locale.current,
    onLocaleChange: (Locale) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    quickAccess: QuickAccess = QuickAccess(),
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
                                        imageVector = if (isDrawerOpen) Icons.Default.Menu else Icons.Default.Menu,
                                        contentDescription = stringResource(Res.string.menu),
                                    )
                                }
                            }

                        if (hasBack)
                            AppTooltipBox(stringResource(Res.string.back)) {
                                IconButton(
                                    onClick = { onNavigationAction(NavigationAction.Pop) },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = stringResource(Res.string.back),
                                    )
                                }
                            }
                    }
                },
                actions = {
                    if (quickAccess.isSupport)
                        AuthComposable(auth) {
                            AppTooltipBox(stringResource(Res.string.help)) {
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = stringResource(Res.string.help),
                                    )
                                }
                            }
                        }

                    if (quickAccess.isTheme)
                        AppTooltipBox(stringResource(Res.string.theme)) {
                            IconButton(
                                onClick = {
                                    onThemeChange(theme.copyIsDarkToggled())
                                },
                            ) {
                                theme.IsDarkIcon()
                            }
                        }

                    if (quickAccess.isLocale) {
                        var isLocalePickerDialogOpen by remember { mutableStateOf(false) }

                        if (isLocalePickerDialogOpen)
                            LocalePickerDialog(
                                onItemClicked = { item ->
                                    onLocaleChange(item)
                                    isLocalePickerDialogOpen = false
                                },
                                onDismissRequest = {
                                    isLocalePickerDialogOpen = false
                                },
                                locales = locales,
                                picker = CountryPicker(
                                    headerTitle = stringResource(Res.string.language),
                                    searchHint = stringResource(Res.string.search),
                                ),
                            )


                        AppTooltipBox(stringResource(Res.string.language)) {
                            Button(
                                onClick = {
                                    isLocalePickerDialogOpen = true
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
                                    painter = painterResource(locale.country()!!.alpha2.flag),
                                    contentDescription = stringResource(Res.string.country_flag),
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .wrapContentWidth(),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }

                    if (quickAccess.isAvatar)
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
