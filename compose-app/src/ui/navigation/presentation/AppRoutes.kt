package ui.navigation.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.window.core.layout.WindowSizeClass
import clib.data.type.primitives.string.asStringResource
import clib.di.koinViewModel
import clib.presentation.auth.LocalAuthState
import clib.presentation.components.model.item.Item
import clib.presentation.locale.LocalLocaleState
import clib.presentation.navigation.AuthRoute
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.LocalRouter
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Route
import clib.presentation.navigation.Routes
import clib.presentation.navigation.model.NavigationItem
import clib.presentation.theme.LocalThemeState
import compose_app.generated.resources.Res
import compose_app.generated.resources.allStringResources
import klib.data.type.auth.AuthResource
import kotlin.reflect.KClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import ui.about.AboutScreen
import ui.auth.forgotpassword.presentation.ForgotPinCodeScreen
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.otp.OtpScreen
import ui.auth.otp.viewmodel.OtpViewModel
import ui.auth.phone.presentation.PhoneScreen
import ui.auth.phone.presentation.viewmodel.PhoneViewModel
import ui.auth.pincode.PinCodeScreen
import ui.auth.pincode.viewmodel.PinCodeViewModel
import ui.auth.profile.presentation.ProfileScreen
import ui.auth.verification.presentation.VerificationScreen
import ui.auth.verification.presentation.viewmodel.VerificationViewModel
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.news.articles.presentation.ArticlesScreen
import ui.news.articles.presentation.viewmodel.ArticleViewModel
import ui.services.ServicesScreen
import ui.settings.SettingsScreen
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.stock.StockScreen

public object App : Routes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Auth, News, Map, Services, Profile, Settings)
    }

    @Composable
    override fun Content(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>,
    ) {
        val themeState = LocalThemeState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val router = LocalRouter.current
        val currentRoute = router.backStack.lastOrNull() ?: return
        val layoutType = if (router.routes?.isNavigationItems(authState.auth) == true) {
            val adaptiveInfo = currentWindowAdaptiveInfo(true)

            if (adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND))
                NavigationSuiteType.NavigationDrawer
            else NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
        else NavigationSuiteType.None

        NavScreen(
            Modifier.fillMaxSize(),
            { Text(text = currentRoute.toString().asStringResource(Res.allStringResources)) },
            themeState.theme,
            { value -> themeState.theme = value },
            localeState.locale,
            { value -> localeState.locale = value },
            authState.auth,
            { value -> authState.auth = value },
            layoutType == NavigationSuiteType.NavigationDrawer,
            router.hasBack,
            layoutType,
            router::actions,
            {
                router.routes?.items(
                    authState.auth,
                    currentRoute,
                    router::push,
                )
            },
        ) {
            super.Content(backStack, onBack, entryProvider)
        }
    }
}

@Serializable
@SerialName("home")
public data object Home : Route<Home>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Home, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Home, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Home) {
        val router = LocalRouter.current

        HomeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("news")
public object News : Routes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Articles)
    }
}

@Serializable
@SerialName("articles")
public data object Articles : Route<Articles>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Newspaper, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Newspaper, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Articles) {
        val viewModel: ArticleViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val router = LocalRouter.current

        ArticlesScreen(
            Modifier,
            route,
            state,
            router::actions,
        )
    }
}

@Serializable
@SerialName("services")
public data object Services : Route<Services>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Apps, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Apps, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Services) {
        val router = LocalRouter.current

        ServicesScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("map")
public data object Map : Route<Map>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Map, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Map, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Map) {
        val router = LocalRouter.current

        MapScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("settings")
public data object Settings : Route<Settings>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Settings, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Settings, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Settings) {
        val scrollState = rememberScrollState()
        val themeState = LocalThemeState.current
        val authState = LocalAuthState.current
        val router = LocalRouter.current

        SettingsScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            themeState.theme,
            { theme -> themeState.theme = theme },
            authState.auth,
            { auth -> authState.auth = auth },
            router::actions,
        )
    }
}

@Serializable
@SerialName("about")
public data object About : Route<About>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Info, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Info, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: About) {
        val router = LocalRouter.current

        AboutScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("auth")
public object Auth : Routes(), AuthRoute {

    override val routes: List<BaseRoute> by lazy {
        listOf(Phone, Otp, PinCode, ForgotPinCode, Verification, Profile)
    }
}

@Serializable
@SerialName("phone")
public data object Phone : Route<Phone>(), NavRoute, AuthRoute {

    @Composable
    override fun Content(route: Phone) {
        val viewModel: PhoneViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val router = LocalRouter.current

        PhoneScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            router::actions,
        )
    }
}

@Serializable
@SerialName("otp")
public data class Otp(val phone: String = "") : NavRoute, AuthRoute {

    override val route: Route<out NavRoute>
        get() = Otp

    public companion object : Route<Otp>() {

        override val navRoute: KClass<out NavRoute>
            get() = Otp::class

        @Composable
        override fun Content(route: Otp) {
            val viewModel: OtpViewModel = koinViewModel { parametersOf(route) }
            val state by viewModel.state.collectAsStateWithLifecycle()
            val router = LocalRouter.current

            OtpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("pinCode")
public data object PinCode : Route<PinCode>(), NavRoute, AuthRoute {

    @Composable
    override fun Content(route: PinCode) {
        val viewModel: PinCodeViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val router = LocalRouter.current

        PinCodeScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            router::actions,
        )
    }
}

@Serializable
@SerialName("login")
public data object Login : Route<Login>(), NavRoute, AuthRoute {

    @Composable
    override fun Content(route: Login) {
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val router = LocalRouter.current

        LoginScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            router::actions,
        )
    }
}

@Serializable
@SerialName("forgotPinCode")
public data object ForgotPinCode : Route<ForgotPinCode>(), NavRoute, AuthRoute {

    @Composable
    override fun Content(route: ForgotPinCode) {
        val router = LocalRouter.current

        ForgotPinCodeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("verification")
public data object Verification : Route<Verification>(), NavRoute {

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Verification) {
        val viewModel: VerificationViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val authState = LocalAuthState.current
        val router = LocalRouter.current

        VerificationScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            state,
            viewModel::action,
            authState.auth,
            { auth -> authState.auth = auth },
            router::actions,
        )
    }
}

@Serializable
@SerialName("profile")
public data object Profile : Route<Profile>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.Person, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.Person, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Profile) {
        val scrollState = rememberScrollState()
        val authState = LocalAuthState.current
        val router = LocalRouter.current

        ProfileScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            authState.auth,
            { auth -> authState.auth = auth },
            router::actions,
        )
    }
}

@Serializable()
@SerialName("wallet")
public object Wallet : Routes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Balance, Crypto, Stock)
    }
}

@Serializable
@SerialName("balance")
public data object Balance : Route<Balance>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.AccountBalance, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.AccountBalance, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Balance) {
        val router = LocalRouter.current

        BalanceScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("crypto")
public data object Crypto : Route<Crypto>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.EnhancedEncryption, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.EnhancedEncryption, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Crypto) {
        val router = LocalRouter.current

        CryptoScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("stock")
public data object Stock : Route<Stock>(), NavRoute {

    override val navigationItem: NavigationItem? = NavigationItem(
        item = Item(
            icon = {
                Icon(Icons.Outlined.CurrencyExchange, toString().asStringResource(Res.allStringResources))
            },
        ),
        selectedItem = Item(
            icon = {
                Icon(Icons.Filled.CurrencyExchange, toString().asStringResource(Res.allStringResources))
            },
        ),
    )

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(route: Stock) {
        val router = LocalRouter.current

        StockScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}
