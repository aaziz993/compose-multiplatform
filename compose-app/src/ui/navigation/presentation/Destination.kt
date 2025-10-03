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
import androidx.navigation.NavDeepLink
import androidx.navigation.navDeepLink
import clib.presentation.components.navigation.model.AbstractDestination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class Destination : AbstractDestination() {

    @Serializable
    public data object NavGraph : Destination() {

        public val rootDeepLinks: List<String> = listOf("https://", "http://")
    }

    @Serializable
    @SerialName("home")
    public data object Home : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = NavGraph.rootDeepLinks.map { navDeepLink<Home>("${it}main") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Home, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Home, label)
        }
    }

    @Serializable
    @SerialName("map")
    public data object Map : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = NavGraph.rootDeepLinks.map { navDeepLink<Map>("${it}map") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Map, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Map, label)
        }
    }

    @Serializable
    @SerialName("settings")
    public data object Settings : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = NavGraph.rootDeepLinks.map { navDeepLink<Settings>("${it}settings") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Settings, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Settings, label)
        }
    }

    @Serializable
    @SerialName("about")
    public data object About : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = NavGraph.rootDeepLinks.map { navDeepLink<About>("${it}about") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Info, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Info, label)
        }
    }

    @Serializable
    public data object AuthGraph : Destination() {

        public val nodeDeepLinks: List<String> = NavGraph.rootDeepLinks.map { "${it}auth/" }
    }

    @Serializable
    @SerialName("login")
    public data object Login : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = AuthGraph.nodeDeepLinks.map { navDeepLink<Login>("${it}login") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.AutoMirrored.Outlined.Login, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.AutoMirrored.Filled.Login, label)
        }
    }

    @Serializable
    @SerialName("forgotpassword")
    public data class ForgotPassword(val username: String) : Destination() {

        public companion object : AbstractDestination() {

            override val deepLinks: List<NavDeepLink> = AuthGraph.nodeDeepLinks.map { navDeepLink<ForgotPassword>("${it}forgotpassword") }
        }
    }

    @Serializable
    @SerialName("profile")
    public data object Profile : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = AuthGraph.nodeDeepLinks.map { navDeepLink<Profile>("${it}profile") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.Person, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.Person, label)
        }
    }

    @Serializable
    public data object WalletGraph : Destination() {

        public val nodeDeepLinks: List<String> = NavGraph.rootDeepLinks.map { "${it}wallet/" }
    }

    @Serializable
    @SerialName("balance")
    public data object Balance : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = WalletGraph.nodeDeepLinks.map { navDeepLink<Balance>("${it}balance") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.AccountBalance, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.AccountBalance, label)
        }
    }

    @Serializable
    @SerialName("crypto")
    public data object Crypto : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = WalletGraph.nodeDeepLinks.map { navDeepLink<Crypto>("${it}crypto") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.EnhancedEncryption, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.EnhancedEncryption, label)
        }
    }

    @Serializable
    @SerialName("stock")
    public data object Stock : Destination() {

        override val deepLinks: List<NavDeepLink>
            get() = WalletGraph.nodeDeepLinks.map { navDeepLink<Stock>("${it}stock") }

        @Composable
        override fun Icon(label: String, modifier: Modifier) {
            Icon(Icons.Outlined.CurrencyExchange, label)
        }

        @Composable
        override fun SelectedIcon(label: String, modifier: Modifier) {
            Icon(Icons.Filled.CurrencyExchange, label)
        }
    }

    public companion object {

        public val destinations: List<Destination> by lazy {
            listOf(Home, Map, Settings, About, Login, Profile, Balance, Crypto, Stock)
        }
    }
}
