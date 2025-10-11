package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.filled.PersonAddAlt
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
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import clib.presentation.auth.stateholder.AuthStateHolder
import clib.presentation.components.navigation.model.NavigationDestination
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.model.Route
import clib.presentation.components.navigation.viewmodel.NavigationAction
import clib.presentation.theme.stateholder.ThemeStateHolder
import klib.data.type.auth.AuthResource
import kotlin.reflect.KClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import presentation.components.scaffold.ScreenAppBar
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.otp.OtpScreen
import ui.auth.otp.viewmodel.OtpViewModel
import ui.auth.phonecheckup.presentation.PhoneCheckUpScreen
import ui.auth.phonecheckup.presentation.viewmodel.PhoneCheckUpViewModel
import ui.auth.pincode.PinCodeScreen
import ui.auth.pincode.viewmodel.PinCodeViewModel
import ui.auth.profile.presentation.ProfileScreen
import ui.auth.unverified.presentation.UnverifiedScreen
import ui.auth.verification.presentation.VerificationScreen
import ui.auth.verification.presentation.viewmodel.VerificationViewModel
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.navigation.presentation.viewmodel.NavViewModel
import ui.news.articles.presentation.ArticlesScreen
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

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Home, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Home, label, modifier)
    }

    @Composable
    override fun Screen(
        route: Home,
        onNavigationAction: (NavigationAction) -> Unit,
    ) {
        HomeScreen(
            Modifier,
            route,
            onNavigationAction,
        )
    }
}

@Serializable
@SerialName("news")
public data object News : Destination, NavigationRoute() {

    override val expand: Boolean = true

    override val routes: List<NavigationDestination<*>> = listOf(Articles)
}

@Serializable
@SerialName("articles")
public data object Articles : Destination, NavigationDestination<Articles>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Newspaper, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Newspaper, label, modifier)
    }

    @Composable
    override fun Screen(
        route: Articles,
        onNavigationAction: (NavigationAction) -> Unit,
    ) {
        val themeStateHolder: ThemeStateHolder = koinInject()
        val authStateHolder: AuthStateHolder = koinInject()
        val navViewModel: NavViewModel = koinViewModel()
        val navState by navViewModel.state.collectAsStateWithLifecycle()

        ScreenAppBar(
            themeStateHolder::action,
            authStateHolder::action,
            navState,
            navViewModel::action,
            onNavigationAction,
            modifier = Modifier.fillMaxSize(),
        ) {
            ArticlesScreen(
                Modifier,
                route,
                onNavigationAction,
            )
        }
    }
}

@Serializable
@SerialName("map")
public data object Map : Destination, NavigationDestination<Map>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Map, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Map, label, modifier)
    }

    @Composable
    override fun Screen(route: Map, onNavigationAction: (NavigationAction) -> Unit): Unit =
        MapScreen(
            Modifier,
            route,
            onNavigationAction,
        )
}

@Serializable
@SerialName("services")
public data object Services : Destination, NavigationDestination<Services>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Apps, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Apps, label, modifier)
    }

    @Composable
    override fun Screen(
        route: Services,
        onNavigationAction: (NavigationAction) -> Unit,
    ) {
        val themeStateHolder: ThemeStateHolder = koinInject()
        val authStateHolder: AuthStateHolder = koinInject()
        val navViewModel: NavViewModel = koinViewModel()
        val navState by navViewModel.state.collectAsStateWithLifecycle()

        ScreenAppBar(
            themeStateHolder::action,
            authStateHolder::action,
            navState,
            navViewModel::action,
            onNavigationAction,
            modifier = Modifier.fillMaxSize(),
        ) {
            ServicesScreen(
                Modifier,
                route,
                onNavigationAction,
            )
        }
    }
}

@Serializable
@SerialName("settings")
public data object Settings : Destination, NavigationDestination<Settings>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Settings, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Settings, label, modifier)
    }

    @Composable
    override fun Screen(route: Settings, onNavigationAction: (NavigationAction) -> Unit) {
        val themeStateHolder: ThemeStateHolder = koinInject()
        val authStateHolder: AuthStateHolder = koinInject()

        SettingsScreen(
            Modifier,
            route,
            themeStateHolder::action,
            authStateHolder::action,
            onNavigationAction,
        )
    }
}

@Serializable
@SerialName("about")
public data object About : Destination, NavigationDestination<About>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Info, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Info, label, modifier)
    }

    @Composable
    override fun Screen(route: About, onNavigationAction: (NavigationAction) -> Unit): Unit =
        AboutScreen(
            Modifier,
            route,
            onNavigationAction,
        )
}

@Serializable
@SerialName("auth")
public data object AuthRoute : Destination, NavigationRoute() {

    override val expand: Boolean = true

    override val routes: List<NavigationDestination<*>> = listOf(PhoneCheckUp, Otp, Unverified, Verification, Login, ForgotPassword, Profile)

    override fun authResource(): AuthResource? = null
}

@Serializable
@SerialName("phonecheckup")
public data object PhoneCheckUp : Destination, NavigationDestination<PhoneCheckUp>() {

    override val deepLinks: List<String> = listOf("phonesignup")

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.PersonAddAlt, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.PersonAddAlt, label, modifier)
    }

    override fun authResource(): AuthResource? = null

    @Composable
    override fun Screen(route: PhoneCheckUp, onNavigationAction: (NavigationAction) -> Unit) {
        val viewModel: PhoneCheckUpViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        PhoneCheckUpScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            onNavigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("pincode")
public data object PinCode : Destination, NavigationDestination<PinCode>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.PersonAddAlt, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.PersonAddAlt, label, modifier)
    }

    override fun authResource(): AuthResource? = null

    @Composable
    override fun Screen(route: PinCode, onNavigationAction: (NavigationAction) -> Unit) {
        val viewModel: PinCodeViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        PinCodeScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            onNavigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("login")
public data object Login : Destination, NavigationDestination<Login>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
    }

    override fun authResource(): AuthResource? = null

    @Composable
    override fun Screen(route: Login, onNavigationAction: (NavigationAction) -> Unit) {
        val viewModel: LoginViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        LoginScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            onNavigationAction,
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

        override val deepLinks: List<String> = listOf("otp")

        override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
        }

        override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
        }

        override fun authResource(): AuthResource? = null

        @Composable
        override fun Screen(route: Otp, onNavigationAction: (NavigationAction) -> Unit) {
            val viewModel: OtpViewModel = koinViewModel()

            val state by viewModel.state.collectAsStateWithLifecycle()

            OtpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                state,
                viewModel::action,
                onNavigationAction,
            )
        }

        override fun isNavigateItem(): Boolean = false
    }
}

@Serializable
@SerialName("forgotpassword")
public data object ForgotPassword : Destination, NavigationDestination<ForgotPassword>() {

    @Composable
    override fun Screen(route: ForgotPassword, onNavigationAction: (NavigationAction) -> Unit) {
        ForgotPasswordScreen(
            Modifier,
            route,
            onNavigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("unverified")
public data object Unverified : Destination, NavigationDestination<Unverified>() {

    @Composable
    override fun Screen(route: Unverified, onNavigationAction: (NavigationAction) -> Unit) {
        UnverifiedScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            onNavigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("verification")
public data object Verification : Destination, NavigationDestination<Verification>() {

    @Composable
    override fun Screen(route: Verification, onNavigationAction: (NavigationAction) -> Unit) {
        val authStateHolder: AuthStateHolder = koinInject()
        val verificationViewModel: VerificationViewModel = koinViewModel()
        val state by verificationViewModel.state.collectAsStateWithLifecycle()

        VerificationScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            authStateHolder::action,
            state,
            verificationViewModel::action,
            onNavigationAction,
        )
    }

    override fun isNavigateItem(): Boolean = false
}

@Serializable
@SerialName("profile")
public data object Profile : Destination, NavigationDestination<Profile>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Person, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Person, label, modifier)
    }

    @Composable
    override fun Screen(route: Profile, onNavigationAction: (NavigationAction) -> Unit) {
        val authStateHolder: AuthStateHolder = koinInject()

        ProfileScreen(
            Modifier,
            route,
            authStateHolder::action,
            onNavigationAction,
        )
    }
}

@Serializable
@SerialName("wallet")
public data object WalletRoute : Destination, NavigationRoute() {

    override val routes: List<NavigationDestination<*>> by lazy { listOf(Balance, Crypto, Stock) }
}

@Serializable
@SerialName("balance")
public data object Balance : Destination, NavigationDestination<Balance>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.AccountBalance, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.AccountBalance, label, modifier)
    }

    @Composable
    override fun Screen(route: Balance, onNavigationAction: (NavigationAction) -> Unit) {
        BalanceScreen(
            Modifier,
            route,
            onNavigationAction,
        )
    }
}

@Serializable
@SerialName("crypto")
public data object Crypto : Destination, NavigationDestination<Crypto>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.EnhancedEncryption, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.EnhancedEncryption, label, modifier)
    }

    @Composable
    override fun Screen(route: Crypto, onNavigationAction: (NavigationAction) -> Unit) {
        CryptoScreen(
            Modifier,
            route,
            onNavigationAction,
        )
    }
}

@Serializable
@SerialName("stock")
public data object Stock : Destination, NavigationDestination<Stock>() {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.CurrencyExchange, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.CurrencyExchange, label, modifier)
    }

    @Composable
    override fun Screen(route: Stock, onNavigationAction: (NavigationAction) -> Unit) {
        StockScreen(
            Modifier,
            route,
            onNavigationAction,
        )
    }
}
