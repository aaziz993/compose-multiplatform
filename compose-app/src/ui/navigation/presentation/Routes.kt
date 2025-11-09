package ui.navigation.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import clib.presentation.components.auth.stateholder.AuthStateHolder
import clib.presentation.components.navigation.NavRoute
import clib.presentation.components.navigation.Route
import clib.presentation.locale.stateholder.LocaleStateHolder
import clib.presentation.theme.stateholder.ThemeStateHolder
import klib.data.type.auth.AuthResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import clib.di.koinInject
import clib.di.koinViewModel
import clib.presentation.components.navigation.stateholder.NavigationStateHolder
import presentation.components.scaffold.AppBar
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPasswordScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.otp.OtpScreen
import ui.auth.otp.viewmodel.OtpViewModel
import ui.auth.phone.presentation.PhoneScreen
import ui.auth.phone.presentation.viewmodel.PhoneViewModel
import ui.auth.pincode.PinCodeScreen
import ui.auth.pincode.viewmodel.PinCodeViewModel
import ui.auth.profile.presentation.ProfileScreen
import ui.auth.unverified.presentation.UnverifiedScreen
import ui.auth.verification.presentation.VerificationScreen
import ui.auth.verification.presentation.viewmodel.VerificationViewModel
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.news.articles.presentation.ArticlesScreen
import ui.services.ServicesScreen
import ui.settings.SettingsScreen
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

public object Routes : List<Route> by listOf(
    Articles,
    Map,
    Services,
    Settings,
)

@Serializable
@SerialName("home")
public data object Home : NavRoute<Home>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Home, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Home, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Home) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        HomeScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("articles")
public data object Articles : NavRoute<Articles>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Newspaper, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Newspaper, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Articles) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        ArticlesScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("map")
public data object Map : NavRoute<Map>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Map, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Map, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Map) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        MapScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("services")
public data object Services : NavRoute<Services>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Apps, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Apps, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Services) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        ServicesScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("settings")
public data object Settings : NavRoute<Settings>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Settings, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Settings, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Settings) {
        val scrollState = rememberScrollState()
        val themeStateHolder: ThemeStateHolder = koinInject()
        val authStateHolder: AuthStateHolder = koinInject()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        SettingsScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            themeStateHolder::action,
            authStateHolder::action,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("about")
public data object About : NavRoute<About>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Info, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Info, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: About) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        AboutScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("phone")
public data object Phone : NavRoute<Phone>(), Route {

    override val route: Phone? = null

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.PersonAddAlt, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.PersonAddAlt, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Phone) {
        val viewModel: PhoneViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        PhoneScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            navigationStateHolder::action,
        )
    }
}

@Serializable
@SerialName("otp")
public data class Otp(val phone: String = "") : Route {

    override val navRoute: NavRoute<Route>
        get() = Otp as NavRoute<Route>

    public companion object : NavRoute<Otp>() {

        override val route: Otp? = null

        override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
        }

        override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
            Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
        }

        @Composable
        override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

        @Composable
        override fun Content(route: Otp) {
            val viewModel: OtpViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val navigationStateHolder: NavigationStateHolder = koinInject()

            OtpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                state,
                viewModel::action,
                navigationStateHolder::action,
            )
        }
    }
}

@Serializable
@SerialName("pincode")
public data object PinCode : NavRoute<PinCode>(), Route {

    override val route: PinCode? = null

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.PersonAddAlt, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.PersonAddAlt, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: PinCode) {
        val viewModel: PinCodeViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        PinCodeScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            navigationStateHolder::action,
        )
    }
}

@Serializable
@SerialName("login")
public data object Login : NavRoute<Login>(), Route {

    override val route: Login? = null

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Outlined.Login, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.AutoMirrored.Filled.Login, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Login) {
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        LoginScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            navigationStateHolder::action,
        )
    }
}

@Serializable
@SerialName("forgotpassword")
public data object ForgotPassword : NavRoute<ForgotPassword>(), Route {

    override val route: ForgotPassword? = null

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: ForgotPassword) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        ForgotPasswordScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }
}

@Serializable
@SerialName("unverified")
public data object Unverified : NavRoute<Unverified>(), Route {

    override val route: Unverified? = null

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Unverified) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        UnverifiedScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("verification")
public data object Verification : NavRoute<Verification>(), Route {

    override val route: Verification? = null

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Verification) {
        val viewModel: VerificationViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val authStateHolder: AuthStateHolder = koinInject()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        VerificationScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            state,
            viewModel::action,
            authStateHolder::action,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("profile")
public data object Profile : NavRoute<Profile>(), Route {

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.Person, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.Person, label, modifier)
    }

    @Composable
    override fun Content(route: Profile) {
        val scrollState = rememberScrollState()
        val authStateHolder: AuthStateHolder = koinInject()
        val navigationStateHolder: NavigationStateHolder = koinInject()

        ProfileScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            authStateHolder::action,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("balance")
public data object Balance : NavRoute<Balance>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.AccountBalance, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.AccountBalance, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Balance) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        BalanceScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("crypto")
public data object Crypto : NavRoute<Crypto>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.EnhancedEncryption, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.EnhancedEncryption, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Crypto) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        CryptoScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Serializable
@SerialName("stock")
public data object Stock : NavRoute<Stock>(), Route {

    override val icon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Outlined.CurrencyExchange, label, modifier)
    }

    override val selectedIcon: @Composable (String, Modifier) -> Unit = { label, modifier ->
        Icon(Icons.Filled.CurrencyExchange, label, modifier)
    }

    @Composable
    override fun ParentContent(content: @Composable () -> Unit): Unit = _ParentContent(content)

    @Composable
    override fun Content(route: Stock) {
        val navigationStateHolder: NavigationStateHolder = koinInject()

        StockScreen(
            Modifier,
            route,
            navigationStateHolder::action,
        )
    }

    override fun authResource(): AuthResource? = AuthResource()
}

@Suppress("FunctionName")
@Composable
private fun _ParentContent(content: @Composable () -> Unit) {
    val themeStateHolder: ThemeStateHolder = koinInject()
    val localeStateHolder: LocaleStateHolder = koinInject()
    val authStateHolder: AuthStateHolder = koinInject()
    val navigationStateHolder: NavigationStateHolder = koinInject()
    val drawerStateHolder: DrawerStateHolder = koinInject()

    AppBar(
        themeStateHolder::action,
        localeStateHolder::action,
        authStateHolder::action,
        navigationStateHolder.backStack.last(),
        navigationStateHolder.canNavigateBack(),
        navigationStateHolder::action,
        drawerStateHolder.isOpen,
        { drawerStateHolder.isOpen = !drawerStateHolder.isOpen },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            content()
        }
    }
}
