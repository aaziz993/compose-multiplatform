package ui.navigation.presentation

import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import clib.data.permission.LocalPermissionsState
import clib.di.koinViewModel
import clib.di.navigation.KoinRoute
import clib.di.navigation.KoinRoutes
import clib.di.navigation.rememberKoinScopeNavEntryDecorator
import clib.presentation.auth.LocalAuthState
import clib.presentation.components.LocalComponentsState
import clib.presentation.components.model.item.Item
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivity
import clib.presentation.locale.LocalLocaleState
import clib.presentation.navigation.AuthRoute
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Route
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.model.NavigationItem
import clib.presentation.navigation.scene.DelegatedScreenStrategy
import clib.presentation.theme.LocalThemeState
import clib.presentation.theme.density.LocalDensityState
import data.type.primitives.string.asStringResource
import klib.data.auth.AuthResource
import klib.data.location.country.Country
import klib.data.location.country.current
import klib.data.location.country.getCountries
import klib.data.type.primitives.string.case.toSnakeCase
import kotlin.reflect.KClass
import kotlinx.coroutines.launch
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

@Serializable
@SerialName("app")
public data object App : KoinRoutes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Auth, News, Map, Services, Profile, Verification, Settings)
    }

    @Composable
    override fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>
    ): Unit = androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberKoinScopeNavEntryDecorator(),
        ),
        sceneStrategy = DelegatedScreenStrategy(
            NavScreenSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("home")
public data object Home : KoinRoute<Home>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = {
                    Icon(Icons.Outlined.Home, text)
                },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = {
                    Icon(Icons.Filled.Home, text)
                },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Home,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        HomeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("news")
public data object News : KoinRoutes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Articles)
    }

    @Composable
    override fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>
    ): Unit = androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberKoinScopeNavEntryDecorator(),
        ),
        sceneStrategy = DelegatedScreenStrategy(
            NavScreenSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("articles")
public data object Articles : KoinRoute<Articles>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Newspaper, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Newspaper, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Articles,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()
        val viewModel: ArticleViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

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
public data object Services : KoinRoute<Services>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Apps, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Apps, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Services,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        ServicesScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("map")
public data object Map : KoinRoute<Map>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Map, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Map, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Map,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        MapScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("settings")
public data object Settings : KoinRoute<Settings>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Settings, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Settings, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Settings,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val config = LocalConfig.current
        val connectivity = LocalConnectivity.current
        val permissionsState = LocalPermissionsState.current
        val componentsState = LocalComponentsState.current
        val themeState = LocalThemeState.current
        val densityState = LocalDensityState.current
        val localeState = LocalLocaleState.current
        val authState = LocalAuthState.current
        val router = currentRouter()

        SettingsScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            connectivity,
            permissionsState.permissions,
            { value ->
                coroutineScope.launch {
                    permissionsState.providePermission(value)
                }
            },
            config.ui.components,
            componentsState.components,
            { value -> componentsState.components = value },
            config.ui.theme,
            themeState.theme,
            { value -> themeState.theme = value },
            config.ui.density,
            densityState.density,
            { value -> densityState.density = value },
            config.localization.locales,
            config.localization.locale,
            localeState.localeInspectionAware(),
            { value -> localeState.locale = value },
            authState.auth,
            { value -> authState.auth = value },
            router::actions,
        )
    }
}

@Serializable
@SerialName("about")
public data object About : KoinRoute<About>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Info, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Info, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: About,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        AboutScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("auth")
public data object Auth : KoinRoutes(), AuthRoute {

    override val routes: List<BaseRoute> by lazy {
        listOf(Phone, Otp, PinCode, ForgotPinCode)
    }

    @Composable
    override fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>
    ): Unit = androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberKoinScopeNavEntryDecorator(),
        ),
        sceneStrategy = DelegatedScreenStrategy(
            NavScreenSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("phone")
public data object Phone : KoinRoute<Phone>(), NavRoute, AuthRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    @Composable
    override fun Content(
        route: Phone,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()
        val viewModel: PhoneViewModel = koinViewModel { parametersOf(router) }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val country = Country.getCountries().find { country -> country.dial == state.countryCode }
            ?: (if (!LocalInspectionMode.current) Country.current else null)
            ?: Country.forCode("US")

        PhoneScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp),
            route,
            state,
            viewModel::action,
            country,
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

        override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

        @Composable
        override fun Content(
            route: Otp,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val router = currentRouter()
            val viewModel: OtpViewModel = koinViewModel { parametersOf(route) }
            val state by viewModel.state.collectAsStateWithLifecycle()

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
public data object PinCode : KoinRoute<PinCode>(), NavRoute, AuthRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    @Composable
    override fun Content(
        route: PinCode,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()
        val viewModel: PinCodeViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

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
public data object Login : KoinRoute<Login>(), NavRoute, AuthRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    @Composable
    override fun Content(
        route: Login,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

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
public data object ForgotPinCode : KoinRoute<ForgotPinCode>(), NavRoute, AuthRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    @Composable
    override fun Content(
        route: ForgotPinCode,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        ForgotPinCodeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("verification")
public data object Verification : KoinRoute<Verification>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Verification,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val authState = LocalAuthState.current
        val router = currentRouter()
        val viewModel: VerificationViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

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
public data object Profile : KoinRoute<Profile>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Person, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Person, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Profile,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val connectivity = LocalConnectivity.current
        val componentsState = LocalComponentsState.current
        val authState = LocalAuthState.current
        val router = currentRouter()

        ProfileScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            connectivity,
            componentsState.components,
            authState.auth,
            { auth -> authState.auth = auth },
            router::actions,
        )
    }

    @Composable
    override fun isNavigationItem(auth: klib.data.auth.model.Auth): Boolean {
        val isAvatar = LocalComponentsState.current.components.quickAccess.isAvatar
        return super.isNavigationItem(auth) && !isAvatar
    }
}

@Serializable()
@SerialName("wallet")
public data object Wallet : KoinRoutes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Balance, Crypto, Stock)
    }

    @Composable
    override fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>
    ): Unit = androidx.navigation3.ui.NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberKoinScopeNavEntryDecorator(),
        ),
        sceneStrategy = DelegatedScreenStrategy(
            NavScreenSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("balance")
public data object Balance : KoinRoute<Balance>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.AccountBalance, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.AccountBalance, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Balance,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        BalanceScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("crypto")
public data object Crypto : KoinRoute<Crypto>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.EnhancedEncryption, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.EnhancedEncryption, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Crypto,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        CryptoScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("stock")
public data object Stock : KoinRoute<Stock>(), NavRoute {

    override val metadata: kotlin.collections.Map<String, Any> = super.metadata + NavScreenSceneStrategy.navScreen()

    override val navigationItem: @Composable (name: String) -> NavigationItem = { name ->
        val text = name.toSnakeCase().asStringResource { name }
        NavigationItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.CurrencyExchange, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.CurrencyExchange, text) },
            ),
        )
    }

    override val authResource: AuthResource? = AuthResource()

    @Composable
    override fun Content(
        route: Stock,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        StockScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}
