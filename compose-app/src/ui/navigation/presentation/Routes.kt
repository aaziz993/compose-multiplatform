package ui.navigation.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.EnhancedEncryption
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Volcano
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.NavigationRoute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.profile.presentation.ProfileScreen
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.settings.SettingsScreen
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

@Serializable
public data object NavRoute : NavigationRoute<NavRoute, NavRoute>() {

    override val deepLinks: List<String> = listOf("https://", "http://")

    override val composableChildren: List<NavigationRoute<NavRoute, *>> =
        listOf(
            Home,
            Map,
            Settings,
            About,
            AuthRoute,
//            WalletRoute,
        )
}

@Serializable
@SerialName("home")
public data object Home : NavigationRoute<NavRoute, Home>() {

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
        navigateTo: (NavigationRoute<NavRoute, *>) -> Unit,
        navigateBack: () -> Unit
    ) {
        HomeScreen(route, navigateTo, navigateBack)
    }
}

@Serializable
@SerialName("map")
public data object Map : NavigationRoute<NavRoute, Map>() {

    override val deepLinks: List<String> = listOf("map")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Map, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Map, label, modifier)
    }

    @Composable
    override fun Screen(route: Map, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        MapScreen(route, navigateTo, navigateBack)
}

@Serializable
@SerialName("settings")
public data object Settings : NavigationRoute<NavRoute, Settings>() {

    override val deepLinks: List<String> = listOf("settings")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Settings, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Settings, label, modifier)
    }

    @Composable
    override fun Screen(route: Settings, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        SettingsScreen(route, navigateTo, navigateBack)
}

@Serializable
@SerialName("about")
public data object About : NavigationRoute<NavRoute, About>() {

    override val deepLinks: List<String> = listOf("about")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Info, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Info, label, modifier)
    }

    @Composable
    override fun Screen(route: About, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        AboutScreen(route, navigateTo, navigateBack)
}

@Serializable
public data object AuthRoute : NavigationRoute<NavRoute, AuthRoute>() {

    override val deepLinks: List<String> = listOf("auth")

    override val composableChildren: List<NavigationRoute<NavRoute, *>> = listOf(Login, Profile)

    override val navigationChildren: List<NavigationRoute<NavRoute, *>> = composableChildren.filterNot { child -> child == ForgotPassword.Companion }
}

@Serializable
@SerialName("login")
public data object Login : NavigationRoute<NavRoute, Login>() {

    override val deepLinks: List<String> = listOf("login")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
    }

    @Composable
    override fun Screen(route: Login, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        LoginScreen(route, navigateTo, navigateBack)
}

public data class ForgotPassword(val username: String) {

    @Serializable
    @SerialName("forgotpassword")
    public companion object : NavigationRoute<NavRoute, ForgotPassword>() {

        override val deepLinks: List<String> = listOf("forgotpassword")

        @Composable
        override fun Screen(route: ForgotPassword, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
            ForgotPasswordScreen(route, navigateTo, navigateBack)
    }
}

@Serializable
@SerialName("profile")
public data object Profile : NavigationRoute<NavRoute, Profile>() {

    override val deepLinks: List<String> = listOf("profile")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Person, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Person, label, modifier)
    }

    @Composable
    override fun Screen(route: Profile, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        ProfileScreen(route, navigateTo, navigateBack)
}

@Serializable
public data object WalletRoute : NavigationRoute<NavRoute, WalletRoute>() {

    override val deepLinks: List<String> = listOf("wallet")

    override val composableChildren: List<NavigationRoute<NavRoute, *>> by lazy { listOf(Balance, Crypto, Stock) }
}

@Serializable
@SerialName("balance")
public data object Balance : NavigationRoute<NavRoute, Balance>() {

    override val deepLinks: List<String> = listOf("balance")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.AccountBalance, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.AccountBalance, label, modifier)
    }

    @Composable
    override fun Screen(route: Balance, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        BalanceScreen(route, navigateTo, navigateBack)
}

@Serializable
@SerialName("crypto")
public data object Crypto : NavigationRoute<NavRoute, Crypto>() {

    override val deepLinks: List<String> = listOf("crypto")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.EnhancedEncryption, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.EnhancedEncryption, label, modifier)
    }

    @Composable
    override fun Screen(route: Crypto, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        CryptoScreen(route, navigateTo, navigateBack)
}

@Serializable
@SerialName("stock")
public data object Stock : NavigationRoute<NavRoute, Stock>() {

    override val deepLinks: List<String> = listOf("stock")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.CurrencyExchange, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.CurrencyExchange, label, modifier)
    }

    @Composable
    override fun Screen(route: Stock, navigateTo: (NavigationRoute<NavRoute, *>) -> Unit, navigateBack: () -> Unit): Unit =
        StockScreen(route, navigateTo, navigateBack)
}
