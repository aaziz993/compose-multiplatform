package presentation.components.scaffold

import androidx.compose.animation.core.EaseIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.filled.ViewSidebar
import androidx.compose.material.icons.automirrored.outlined.ViewSidebar
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.location.country.flag
import clib.data.type.primitives.string.stringResource
import clib.presentation.appbar.model.AppBar
import clib.presentation.appbar.model.AppBarMode
import clib.presentation.auth.AuthComposable
import clib.presentation.components.country.LocalePickerDialog
import clib.presentation.components.country.model.CountryPicker
import clib.presentation.components.image.avatar.Avatar
import clib.presentation.connectivity.model.Connectivity
import clib.presentation.easedVerticalGradient
import clib.presentation.navigation.NavigationAction
import clib.presentation.theme.model.Theme
import compose_app.generated.resources.Res
import compose_app.generated.resources.avatar
import compose_app.generated.resources.back
import compose_app.generated.resources.clear
import compose_app.generated.resources.country_flag
import compose_app.generated.resources.help
import compose_app.generated.resources.locale
import compose_app.generated.resources.menu
import compose_app.generated.resources.navigate
import compose_app.generated.resources.profile
import compose_app.generated.resources.search
import compose_app.generated.resources.sign_out
import compose_app.generated.resources.theme
import data.location.locale.asStringResource
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import dev.jordond.connectivity.Connectivity.Status
import klib.auth.model.Auth
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import org.jetbrains.compose.resources.painterResource
import presentation.components.dialog.SignOutConfirmDialog
import presentation.components.tooltipbox.PlainTooltipBox
import presentation.connectivity.CircleIcon
import presentation.connectivity.DefaultIcon
import presentation.connectivity.Text
import presentation.connectivity.stringResource
import presentation.theme.model.IsDarkIcon
import ui.navigation.presentation.Profile
import ui.navigation.presentation.Support

@Composable
public fun TopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    connectivityStatus: Status = Status.Disconnected,
    appBar: AppBar = AppBar(),
    connectivity: Connectivity = Connectivity(),
    blurEnabled: Boolean = HazeDefaults.blurEnabled(),
    inputScale: HazeInputScale = HazeInputScale.Default,
    theme: Theme = Theme(),
    onThemeChange: (Theme) -> Unit = {},
    locales: List<Locale> = emptyList(),
    locale: Locale = Locale.current,
    onLocaleChange: (Locale) -> Unit = {},
    auth: Auth = Auth(),
    onAuthChange: (Auth) -> Unit = {},
    hasDrawer: Boolean = true,
    isDrawerOpen: Boolean = true,
    onDrawerToggle: () -> Unit = {},
    hasBack: Boolean = true,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit = {},
) {
    val hazeState = rememberHazeState(blurEnabled = blurEnabled)
    val style = HazeMaterials.regular(MaterialTheme.colorScheme.surface)

    Scaffold(
        modifier = modifier
            .hazeEffect(state = hazeState, style = style) {
                this.inputScale = inputScale

                when (appBar.mode) {
                    AppBarMode.Default -> Unit
                    AppBarMode.Progressive ->
                        progressive = HazeProgressive.verticalGradient(
                            startIntensity = 1f,
                            endIntensity = 0f,
                        )

                    AppBarMode.Mask -> mask = Brush.easedVerticalGradient(EaseIn)
                }
            },
        topBar = {
            TopAppBar(
                title = {
                    if (appBar.isTitle) title()
                },
                navigationIcon = {
                    Row {
                        if (hasDrawer)
                            IconButton(
                                onClick = onDrawerToggle,
                            ) {
                                PlainTooltipBox(tooltip = stringResource(Res.string.menu)) {
                                    Icon(
                                        imageVector = if (isDrawerOpen) Icons.AutoMirrored.Filled.ViewSidebar else Icons.AutoMirrored.Outlined.ViewSidebar,
                                        contentDescription = stringResource(Res.string.menu),
                                    )
                                }
                            }

                        if (hasBack)
                            IconButton(
                                onClick = { onNavigationActions(arrayOf(NavigationAction.Pop)) },
                            ) {
                                PlainTooltipBox(tooltip = stringResource(Res.string.back)) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.NavigateBefore,
                                        contentDescription = stringResource(Res.string.back),
                                    )
                                }
                            }
                    }
                },
                actions = {
                    Row(modifier = Modifier.wrapContentSize()) {
                        if (connectivity.isConnectivityIndicatorText)
                            connectivityStatus.Text(overflow = TextOverflow.Clip, maxLines = 1)

                        if (connectivity.isConnectivityIndicator) {
                            PlainTooltipBox(tooltip = connectivityStatus.stringResource()) {
                                connectivityStatus.DefaultIcon()
                            }
                        }
                    }
                    if (appBar.isSupport)
                        AuthComposable(auth = auth) {
                            IconButton(
                                onClick = {
                                    onNavigationActions(
                                        arrayOf(
                                            NavigationAction.Push(Support),
                                        ),
                                    )
                                },
                            ) {
                                PlainTooltipBox(tooltip = stringResource(Res.string.help)) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = stringResource(Res.string.help),
                                    )
                                }
                            }
                        }

                    if (appBar.isTheme)
                        IconButton(
                            onClick = {
                                onThemeChange(theme.copyIsDarkToggled())
                            },
                        ) {
                            PlainTooltipBox(tooltip = stringResource(Res.string.theme)) {
                                theme.IsDarkIcon()
                            }
                        }

                    if (appBar.isLocale) {
                        var localePickerDialog by remember { mutableStateOf(false) }

                        if (localePickerDialog)
                            LocalePickerDialog(
                                onItemClicked = { item ->
                                    onLocaleChange(item)
                                    localePickerDialog = false
                                },
                                onDismissRequest = {
                                    localePickerDialog = false
                                },
                                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                                locales = locales,
                                country = { locale ->
                                    locale.country()!!.copy(name = locale.asStringResource())
                                },
                                picker = CountryPicker(
                                    headerTitle = stringResource(Res.string.locale),
                                    searchHint = stringResource(Res.string.search),
                                    clear = stringResource(Res.string.clear),
                                    showCountryDial = false,
                                ),
                            )

                        Button(
                            onClick = {
                                localePickerDialog = true
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
                            PlainTooltipBox(tooltip = stringResource(Res.string.locale)) {
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

                    if (appBar.isAvatar) {
                        AuthComposable(auth = auth) { user ->
                            var expanded by remember { mutableStateOf(false) }
                            var singOutDialog by remember { mutableStateOf(false) }
                            Box {
                                PlainTooltipBox(tooltip = stringResource(Res.string.profile)) {
                                    Avatar(
                                        user = user,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .combinedClickable(
                                                onLongClick = { expanded = true },
                                            ) {
                                                onNavigationActions(
                                                    arrayOf(
                                                        NavigationAction.Push(Profile),
                                                    ),
                                                )
                                            },
                                        contentDescription = stringResource(Res.string.avatar),
                                    )
                                    if (connectivity.isAvatarConnectivityIndicator)
                                        PlainTooltipBox(tooltip = connectivityStatus.stringResource()) {
                                            connectivityStatus.CircleIcon(
                                                Modifier
                                                    .align(Alignment.TopStart)
                                                    .size(14.dp),
                                                Modifier
                                                    .align(Alignment.TopStart)
                                                    .size(14.dp),
                                            )
                                        }
                                }

                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = stringResource(Res.string.navigate), color = MaterialTheme.colorScheme.onSurface)
                                        },
                                        onClick = {
                                            onNavigationActions(
                                                arrayOf(
                                                    NavigationAction.Push(Profile),
                                                ),
                                            )
                                            expanded = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.NavigateNext,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface,
                                            )
                                        },
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = stringResource(Res.string.sign_out), color = MaterialTheme.colorScheme.onSurface)
                                        },
                                        onClick = {
                                            expanded = false
                                            singOutDialog = true
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.Logout,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface,
                                            )
                                        },
                                    )
                                }

                                if (singOutDialog)
                                    SignOutConfirmDialog(
                                        {
                                            singOutDialog = false
                                        },
                                        {
                                            onAuthChange(Auth())
                                        },
                                    )
                            }
                        }
                    }
                },
                expandedHeight = appBar.expandedHeight,
                colors = appBar.colors,
            )
        },
        content = content,
    )
}

@Preview
@Composable
private fun PreviewTopAppBar(): Unit = TopAppBar()
