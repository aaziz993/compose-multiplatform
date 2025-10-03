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
import androidx.navigation.NavType
import androidx.navigation.navDeepLink
import clib.presentation.components.navigation.model.AbstractDestination
import kotlin.reflect.KType
import kotlinx.serialization.Serializable
import clib.presentation.components.navigation.model.NavigationItem
import kotlin.collections.Map as MAP

@Serializable
public sealed class Destination : AbstractDestination() {

    override fun item(label: AbstractDestination. () -> String): NavigationItem = label().let { label ->
        NavigationItem(
            modifier,
            selectedModifier,
            enabled,
            alwaysShowLabel,
            { modifier -> Text(label, modifier) },
            { SelectedText(label, modifier) },
            { modifier -> Icon(label, modifier) },
            { modifier -> SelectedIcon(label, modifier) },
            { modifier -> Badge(label, modifier) },
            { modifier -> SelectedBadge(label, modifier) },
        )
    }

    @Serializable
    public data object NavGraph : Destination() {

        public val deepLinks: List<String> = listOf("https://", "http://")
    }

    @Serializable
    public data object Home : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = NavGraph.deepLinks.map { navDeepLink<Home>("${it}main") }

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
    public data object Map : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = NavGraph.deepLinks.map { navDeepLink<Map>("${it}map") }

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
    public data object Settings : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = NavGraph.deepLinks.map { navDeepLink<Settings>("${it}settings") }

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
    public data object About : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = NavGraph.deepLinks.map { navDeepLink<About>("${it}about") }

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

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<String>
            get() = NavGraph.deepLinks.map { "${it}auth/" }
    }

    @Serializable
    public data object Login : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = AuthGraph.deepLinks.map { navDeepLink<Login>("${it}login") }

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
    public data class ForgotPassword(val username: String) : Destination() {

        public companion object {

            public val typeMap: MAP<KType, NavType<*>> = emptyMap()

            public val deepLinks: List<NavDeepLink> = AuthGraph.deepLinks.map { navDeepLink<ForgotPassword>("${it}forgotpassword") }
        }
    }

    @Serializable
    public data object Profile : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = AuthGraph.deepLinks.map { navDeepLink<Profile>("${it}profile") }

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

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<String>
            get() = NavGraph.deepLinks.map { "${it}wallet/" }
    }

    @Serializable
    public data object Balance : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = WalletGraph.deepLinks.map { navDeepLink<Balance>("${it}balance") }

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
    public data object Crypto : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = WalletGraph.deepLinks.map { navDeepLink<Crypto>("${it}crypto") }

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
    public data object Stock : Destination() {

        public val typeMap: MAP<KType, NavType<*>> = emptyMap()

        public val deepLinks: List<NavDeepLink>
            get() = WalletGraph.deepLinks.map { navDeepLink<Stock>("${it}stock") }

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

        public val destinations: List<Destination> = listOf(
            Home,
            Map,
            Settings,
            About,
            Login,
            Profile,
            Balance,
            Crypto,
            Stock,
        )
    }
}
