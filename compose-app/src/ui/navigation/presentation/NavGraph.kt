package ui.navigation.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.EnhancedEncryption
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.navigation.LocalBackButton
import clib.presentation.components.navigation.LocalTitle
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.LocalAppTheme
import clib.presentation.theme.ThemeState
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import klib.data.type.auth.AuthResource
import kotlin.reflect.KClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.tooltipbox.AppTooltipBox
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.profile.presentation.ProfileScreen
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.navigation.presentation.viewmodel.NavAction
import ui.navigation.presentation.viewmodel.NavViewModel
import ui.news.NewsScreen
import ui.services.ServicesScreen
import ui.settings.SettingsScreen
import ui.settings.viewmodel.SettingsAction
import ui.settings.viewmodel.SettingsViewModel
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

public sealed interface Destination

@Serializable
public data object NavRoute : Destination, NavigationRoute() {

    override val deepLinks: List<String> = listOf("https://", "http://")

    override val routes: List<Route> =
        listOf(
//            Home,
            News,
            Map,
            Services,
            Settings,
//            About,
            AuthRoute,
//            WalletRoute,
        )

    override fun authResource(): AuthResource? = null
}

@Serializable
@SerialName("home")
public data object Home : Destination, NavigationDestination<Home>() {

    override val deepLinks: List<String> = listOf("main")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Home, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Home, label, modifier)
    }

    @Composable
    override fun Screen(
        route: Home,
        navigationAction: (NavigationAction) -> Unit,
    ) {
        HomeScreen(route, navigationAction)
    }
}

@Serializable
@SerialName("news")
public data object News : Destination, NavigationDestination<News>() {

    override val deepLinks: List<String> = listOf("main")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Newspaper, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Newspaper, label, modifier)
    }

    @Composable
    override fun Screen(
        route: News,
        navigationAction: (NavigationAction) -> Unit,
    ) {
        NewsScreen(route, navigationAction)
    }
}

@Serializable
@SerialName("map")
public data object Map : Destination, NavigationDestination<Map>() {

    override val deepLinks: List<String> = listOf("map")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Map, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Map, label, modifier)
    }

    @Composable
    override fun Screen(route: Map, navigationAction: (NavigationAction) -> Unit): Unit =
        MapScreen(route, navigationAction)
}

@Serializable
@SerialName("services")
public data object Services : Destination, NavigationDestination<Services>() {

    override val deepLinks: List<String> = listOf("main")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Apps, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Apps, label, modifier)
    }

    @Composable
    override fun Screen(
        route: Services,
        navigationAction: (NavigationAction) -> Unit,
    ) {
        val navViewModel: NavViewModel = koinViewModel()
        val navState by navViewModel.state.collectAsStateWithLifecycle()

        val settingsViewModel: SettingsViewModel = koinViewModel()
        val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()

        ScreenAppBar(
            settingsState.themeState,
            { value ->
                settingsViewModel.action(SettingsAction.SetTheme(value))
            },
            navState.drawerOpen,
            { value ->
                navViewModel.action(NavAction.OpenDrawer(value))
            },
            navigationAction,
        ) {
            ServicesScreen(route, navigationAction)
        }
    }
}

@Serializable
@SerialName("settings")
public data object Settings : Destination, NavigationDestination<Settings>() {

    override val deepLinks: List<String> = listOf("settings")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Settings, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Settings, label, modifier)
    }

    @Composable
    override fun Screen(route: Settings, navigationAction: (NavigationAction) -> Unit) {
        val viewModel: SettingsViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        SettingsScreen(route, state, viewModel::action, navigationAction)
    }
}

@Serializable
@SerialName("about")
public data object About : Destination, NavigationDestination<About>() {

    override val deepLinks: List<String> = listOf("about")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Info, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Info, label, modifier)
    }

    @Composable
    override fun Screen(route: About, navigationAction: (NavigationAction) -> Unit): Unit =
        AboutScreen(route, navigationAction)
}

@Serializable
public data object AuthRoute : Destination, NavigationRoute() {

    override val deepLinks: List<String> = listOf("auth")

    override val routes: List<NavigationDestination<*>> = listOf(Login, ForgotPassword, Profile)

    override fun authResource(): AuthResource? = null
}

@Serializable
@SerialName("login")
public data object Login : Destination, NavigationDestination<Login>() {

    override val deepLinks: List<String> = listOf("login")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
    }

    override fun authResource(): AuthResource? = null

    @Composable
    override fun Screen(route: Login, navigationAction: (NavigationAction) -> Unit): Unit {
        val viewModel: LoginViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        LoginScreen(route, state, viewModel::action, Services, navigationAction)
    }

    override fun navigate(): Boolean = false
}

public data class ForgotPassword(val username: String) : Destination {

    @Serializable
    @SerialName("forgotpassword")
    public companion object : NavigationDestination<ForgotPassword>() {

        override val kClass: KClass<ForgotPassword>
            get() = ForgotPassword::class

        override val deepLinks: List<String> = listOf("forgotpassword")

        @Composable
        override fun Screen(route: ForgotPassword, navigationAction: (NavigationAction) -> Unit): Unit =
            ForgotPasswordScreen(route, navigationAction)

        override fun navigate(): Boolean = true
    }
}

@Serializable
@SerialName("profile")
public data object Profile : Destination, NavigationDestination<Profile>() {

    override val deepLinks: List<String> = listOf("profile")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Person, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Person, label, modifier)
    }

    @Composable
    override fun Screen(route: Profile, navigationAction: (NavigationAction) -> Unit): Unit =
        ProfileScreen(route, navigationAction)
}

@Serializable
public data object WalletRoute : Destination, NavigationRoute() {

    override val deepLinks: List<String> = listOf("wallet")

    override val routes: List<NavigationDestination<*>> by lazy { listOf(Balance, Crypto, Stock) }
}

@Serializable
@SerialName("balance")
public data object Balance : Destination, NavigationDestination<Balance>() {

    override val deepLinks: List<String> = listOf("balance")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.AccountBalance, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.AccountBalance, label, modifier)
    }

    @Composable
    override fun Screen(route: Balance, navigationAction: (NavigationAction) -> Unit): Unit =
        BalanceScreen(route, navigationAction)
}

@Serializable
@SerialName("crypto")
public data object Crypto : Destination, NavigationDestination<Crypto>() {

    override val deepLinks: List<String> = listOf("crypto")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.EnhancedEncryption, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.EnhancedEncryption, label, modifier)
    }

    @Composable
    override fun Screen(route: Crypto, navigationAction: (NavigationAction) -> Unit): Unit =
        CryptoScreen(route, navigationAction)
}

@Serializable
@SerialName("stock")
public data object Stock : Destination, NavigationDestination<Stock>() {

    override val deepLinks: List<String> = listOf("stock")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.CurrencyExchange, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.CurrencyExchange, label, modifier)
    }

    @Composable
    override fun Screen(route: Stock, navigationAction: (NavigationAction) -> Unit): Unit =
        StockScreen(route, navigationAction)
}

@Composable
private fun ScreenAppBar(
    themeState: ThemeState,
    onThemeChange: (Theme) -> Unit,
    isDrawerOpen: Boolean,
    onDrawerOpenChange: (Boolean) -> Unit,
    navigationAction: (NavigationAction) -> Unit,
    content: @Composable () -> Unit
) {
//    var isDrawerOpen by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LocalTitle.current) },
                navigationIcon = {
                    Row {
                        if (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                            AppTooltipBox("Menu") {
                                IconButton(
                                    onClick = {
                                        onDrawerOpenChange(!isDrawerOpen)
                                    },
                                ) {
                                    Icon(
                                        imageVector = if (isDrawerOpen) Icons.Filled.Menu else Icons.Outlined.Menu,
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
                    val themeMode = LocalAppTheme.current.mode
                    AppTooltipBox("Switch theme") {
                        IconButton(
                            onClick = {
                                when (themeMode) {
                                    ThemeMode.SYSTEM -> onThemeChange(themeState.theme.copy(mode = ThemeMode.LIGHT))
                                    ThemeMode.LIGHT -> onThemeChange(themeState.theme.copy(mode = ThemeMode.DARK))
                                    ThemeMode.DARK -> onThemeChange(themeState.theme.copy(mode = ThemeMode.SYSTEM))
                                }
                            },
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    ThemeMode.SYSTEM -> Icons.Outlined.SettingsBrightness
                                    ThemeMode.LIGHT -> Icons.Outlined.LightMode
                                    ThemeMode.DARK -> Icons.Outlined.DarkMode
                                },
                                contentDescription = "Switch theme",
                            )
                        }
                    }
                },
            )
        },
    ) {
        content()
    }
}
