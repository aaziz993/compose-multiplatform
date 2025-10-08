package ui.navigation.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.EnhancedEncryption
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import kotlin.reflect.KClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.profile.presentation.ProfileScreen
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.news.NewsScreen
import ui.services.ServicesScreen
import ui.settings.SettingsScreen
import ui.settings.viewmodel.SettingsViewModel
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

public sealed interface Destination

@Serializable
public data object NavRoute : NavigationRoute() {

    override val deepLinks: List<String> = listOf("https://", "http://")

    override val composableChildren: List<Route> =
        listOf(
            Home,
            News,
            Map,
            Services,
            Settings,
            About,
            AuthRoute,
            WalletRoute,
        )
}

@Serializable
@SerialName("home")
public data object Home : Destination, NavigationDestination<Home>() {

    override val excluded: Boolean = true

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
        ServicesScreen(route, navigationAction)
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

    override val excluded: Boolean = true

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
public data object AuthRoute : NavigationRoute() {

    override val excluded: Boolean = true

    override val deepLinks: List<String> = listOf("auth")

    override val composableChildren: List<NavigationDestination<*>> = listOf(Login, Profile)

    override val navigationChildren: List<NavigationDestination<*>> = composableChildren.filterNot { child -> child == ForgotPassword.Companion }
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

    @Composable
    override fun Screen(route: Login, navigationAction: (NavigationAction) -> Unit): Unit {
        val viewModel: LoginViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        LoginScreen(route, state, viewModel::action, navigationAction)
    }
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
public data object WalletRoute : NavigationRoute() {

    override val excluded: Boolean = true

    override val deepLinks: List<String> = listOf("wallet")

    override val composableChildren: List<NavigationDestination<*>> by lazy { listOf(Balance, Crypto, Stock) }
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
