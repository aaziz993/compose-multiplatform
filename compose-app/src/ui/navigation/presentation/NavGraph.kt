package ui.navigation.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import clib.presentation.components.navigation.LocalBackButton
import clib.presentation.components.navigation.LocalTitle
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.model.Theme
import clib.presentation.theme.model.ThemeMode
import clib.presentation.theme.viewmodel.ThemeAction
import clib.presentation.theme.viewmodel.ThemeViewModel
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
import ui.auth.otp.OtpScreen
import ui.auth.otp.viewmodel.OtpViewModel
import ui.services.ServicesScreen
import ui.settings.SettingsScreen
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
        HomeScreen(
            Modifier,
            route,
            navigationAction,
        )
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
        NewsScreen(
            Modifier,
            route,
            navigationAction,
        )
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
        MapScreen(
            Modifier,
            route,
            navigationAction,
        )
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

        val themeViewModel: ThemeViewModel = koinViewModel()
        val theme by themeViewModel.state.collectAsStateWithLifecycle()

        ScreenAppBar(
            theme,
            { value ->
                themeViewModel.action(ThemeAction.SetTheme(value))
            },
            navState.drawerOpen,
            { value ->
                navViewModel.action(NavAction.OpenDrawer(value))
            },
            navigationAction,
        ) {
            ServicesScreen(
                Modifier,
                route,
                navigationAction,
            )
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
        val themeViewModel: ThemeViewModel = koinViewModel()

        val theme by themeViewModel.state.collectAsStateWithLifecycle()

        SettingsScreen(
            Modifier,
            route,
            theme,
            themeViewModel::action,
            navigationAction,
        )
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
        AboutScreen(
            Modifier,
            route,
            navigationAction,
        )
}

@Serializable
public data object AuthRoute : Destination, NavigationRoute() {

    override val deepLinks: List<String> = listOf("auth")

    override val expand: Boolean = true

    override val routes: List<NavigationDestination<*>> = listOf(Login, Otp, ForgotPassword, Profile)

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
    override fun Screen(route: Login, navigationAction: (NavigationAction) -> Unit) {
        val viewModel: LoginViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        LoginScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            navigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("otp")
public data class Otp(val phone: String = "") : Destination {

    public companion object : NavigationDestination<Otp>() {

        override val route: KClass<Otp>
            get() = Otp::class

        override val deepLinks: List<String> = listOf("login")

        override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
        }

        override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
        }

        override fun authResource(): AuthResource? = null

        @Composable
        override fun Screen(route: Otp, navigationAction: (NavigationAction) -> Unit) {
            val viewModel: OtpViewModel = koinViewModel()

            val state by viewModel.state.collectAsStateWithLifecycle()

            OtpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                state,
                viewModel::action,
                Services,
                navigationAction,
            )
        }

        override fun isNavigateItem(): Boolean = false
    }
}

@Serializable
@SerialName("forgotpassword")
public data class ForgotPassword(val username: String = "") : Destination {

    public companion object : NavigationDestination<ForgotPassword>() {

        override val route: KClass<ForgotPassword>
            get() = ForgotPassword::class

        override val deepLinks: List<String> = listOf("forgotpassword")

        @Composable
        override fun Screen(route: ForgotPassword, navigationAction: (NavigationAction) -> Unit) {
            ForgotPasswordScreen(
                Modifier,
                route,
                navigationAction,
            )
        }

        override fun isNavigateItem(): Boolean = false
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
    override fun Screen(route: Profile, navigationAction: (NavigationAction) -> Unit) {
        ProfileScreen(
            Modifier,
            route,
            navigationAction,
        )
    }
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
    override fun Screen(route: Balance, navigationAction: (NavigationAction) -> Unit) {
        BalanceScreen(
            Modifier,
            route,
            navigationAction,
        )
    }
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
    override fun Screen(route: Crypto, navigationAction: (NavigationAction) -> Unit) {
        CryptoScreen(
            Modifier,
            route,
            navigationAction,
        )
    }
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
    override fun Screen(route: Stock, navigationAction: (NavigationAction) -> Unit) {
        StockScreen(
            Modifier,
            route,
            navigationAction,
        )
    }
}

@Composable
private fun ScreenAppBar(
    theme: Theme,
    onThemeChange: (Theme) -> Unit,
    isOpenDrawer: Boolean,
    onOpenDrawerChange: (Boolean) -> Unit,
    navigationAction: (NavigationAction) -> Unit,
    content: @Composable () -> Unit
) {
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
                },
            )
        },
    ) {
        content()
    }
}
