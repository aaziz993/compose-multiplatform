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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.AbstractRoute
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
public sealed class Route : AbstractRoute() {

    @Serializable
    public data object NavGraph : Route() {

        override val deepLinks: List<String> = listOf("https://", "http://")

        override val composableChildren: List<Route> by lazy {
            listOf(Home, Map, Settings, About, Login, Profile, Balance, Crypto, Stock)
        }
    }

    @Serializable
    @SerialName("home")
    public data object Home : Route() {

        override val deepLinks: List<String> = listOf("main")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Home, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Home, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            HomeScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("map")
    public data object Map : Route() {

        override val deepLinks: List<String> = listOf("map")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Map, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Map, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            MapScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("settings")
    public data object Settings : Route() {

        override val deepLinks: List<String> = listOf("settings")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Settings, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Settings, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            SettingsScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("about")
    public data object About : Route() {

        override val deepLinks: List<String> = listOf("about")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Info, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Info, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            AboutScreen(navigateTo, navigateBack)
    }

    @Serializable
    public data object AuthGraph : Route() {

        override val deepLinks: List<String> = listOf("auth")

        override val composableChildren: List<AbstractRoute> by lazy { listOf(Login, ForgotPassword, Profile) }
        override val navigationChildren: List<AbstractRoute> by lazy { listOf(Login, Profile) }
    }

    @Serializable
    @SerialName("login")
    public data object Login : Route() {

        override val deepLinks: List<String> = listOf("login")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.AutoMirrored.Outlined.Login, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.AutoMirrored.Filled.Login, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            LoginScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("forgotpassword")
    public data class ForgotPassword(val username: String) : Route() {

        public companion object : AbstractRoute() {

            override val deepLinks: List<String> = listOf("forgotpassword")
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            ForgotPasswordScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("profile")
    public data object Profile : Route() {

        override val deepLinks: List<String> = listOf("profile")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Person, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Person, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            ProfileScreen(navigateTo, navigateBack)
    }

    @Serializable
    public data object WalletGraph : Route() {

        override val deepLinks: List<String> = listOf("wallet")

        override val composableChildren: List<AbstractRoute> by lazy { listOf(Balance, Crypto, Stock) }
    }

    @Serializable
    @SerialName("balance")
    public data object Balance : Route() {

        override val enabled: Boolean = false

        override val deepLinks: List<String> = listOf("balance")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.AccountBalance, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.AccountBalance, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            BalanceScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("crypto")
    public data object Crypto : Route() {

        override val enabled: Boolean = false

        override val deepLinks: List<String> = listOf("crypto")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.EnhancedEncryption, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.EnhancedEncryption, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            CryptoScreen(navigateTo, navigateBack)
    }

    @Serializable
    @SerialName("stock")
    public data object Stock : Route() {

        override val enabled: Boolean = false

        override val deepLinks: List<String> = listOf("stock")

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.CurrencyExchange, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.CurrencyExchange, label)
        }

        @Composable
        override fun Screen(navigateTo: (AbstractRoute) -> Unit, navigateBack: () -> Unit): Unit =
            StockScreen(navigateTo, navigateBack)
    }
}
