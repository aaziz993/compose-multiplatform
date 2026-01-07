package ui.navigation.presentation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Wallet
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
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontStyle.Companion.Normal
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.text.font.FontSynthesis.Companion.None
import androidx.compose.ui.text.font.FontSynthesis.Companion.Style
import androidx.compose.ui.text.font.FontSynthesis.Companion.Weight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import clib.permission.LocalPermissionsState
import clib.data.share.LocalShare
import clib.di.koinViewModel
import clib.di.navigation.KoinRoute
import clib.di.navigation.KoinRoutes
import clib.di.navigation.rememberKoinScopeNavEntryDecorator
import clib.presentation.appbar.LocalAppBarState
import clib.presentation.auth.LocalAuthState
import clib.presentation.components.model.item.Item
import clib.presentation.components.model.item.SelectableItem
import clib.presentation.config.LocalConfig
import clib.presentation.connectivity.LocalConnectivityState
import clib.presentation.connectivity.LocalConnectivityStatus
import clib.presentation.locale.LocalLocaleState
import clib.presentation.locale.inspectionModeAware
import clib.presentation.navigation.BaseRoute
import clib.presentation.navigation.LocalRoutesState
import clib.presentation.navigation.NavRoute
import clib.presentation.navigation.Route
import clib.presentation.navigation.currentRouter
import clib.presentation.navigation.scene.DelegatedScreenStrategy
import clib.presentation.theme.LocalThemeState
import clib.presentation.theme.density.LocalDensityState
import data.type.primitives.string.asStringResource
import klib.auth.model.Auth
import klib.auth.otp.model.HotpConfig
import klib.auth.otp.model.TotpConfig
import klib.data.location.country.Country
import klib.data.location.country.current
import klib.data.location.country.getCountries
import klib.data.validator.Validator
import kotlin.reflect.KClass
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import ui.about.AboutScreen
import ui.auth.emal.presentation.EmailScreen
import ui.auth.emal.presentation.viewmodel.EmailViewModel
import ui.auth.hotp.HotpScreen
import ui.auth.hotp.viewmodel.HotpViewModel
import ui.auth.login.presentation.LoginScreen
import ui.auth.login.presentation.viewmodel.LoginViewModel
import ui.auth.phone.presentation.PhoneScreen
import ui.auth.phone.presentation.viewmodel.PhoneViewModel
import ui.auth.pincode.PinCodeScreen
import ui.auth.pincode.viewmodel.PinCodeViewModel
import ui.profile.presentation.ProfileScreen
import ui.profile.presentation.viewmodel.ProfileViewModel
import ui.auth.resetpincode.presentation.ResetPinCodeScreen
import ui.auth.totp.TotpScreen
import ui.auth.totp.viewmodel.TotpViewModel
import ui.auth.verification.presentation.VerificationScreen
import ui.auth.verification.presentation.viewmodel.VerificationViewModel
import ui.home.HomeScreen
import ui.map.MapScreen
import ui.news.articledetails.ArticleDetailsScreen
import ui.news.articledetails.viewmodel.ArticleDetailsViewModel
import ui.news.articles.presentation.ArticlesScreen
import ui.news.articles.presentation.viewmodel.ArticleViewModel
import ui.services.ServicesScreen
import ui.settings.SettingsColorSchemeScreen
import ui.settings.SettingsDynamicColorSchemeScreen
import ui.settings.SettingsMainScreen
import ui.settings.SettingsRouteScreen
import ui.settings.SettingsShapesScreen
import ui.settings.SettingsTypographyScreen
import ui.support.SupportScreen
import ui.wallet.balance.BalanceScreen
import ui.wallet.crypto.CryptoScreen
import ui.wallet.exchange.ExchangeScreen

@Serializable
@SerialName("application")
public data object Application : KoinRoutes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Authentification, News, Map, Services, Profile, Verification, Settings, Support)
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
            TopAppBarSceneStrategy().delegate() +
                NavSuiteSceneStrategy().delegate() +
                TopAppBarNavSuiteSceneStrategy().delegate() +
                NavSuiteTopAppBarSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("authentification")
public data object Authentification : KoinRoutes() {

    override val routes: List<BaseRoute> by lazy {
        listOf(Phone, Email, Hotp, Totp, PinCode, ResetPinCode, Login)
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
            TopAppBarSceneStrategy().delegate() +
                NavSuiteSceneStrategy().delegate() +
                TopAppBarNavSuiteSceneStrategy().delegate() +
                NavSuiteTopAppBarSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("phone")
public data class Phone(val username: String? = null) : NavRoute {

    override val route: Route<out NavRoute>
        get() = Phone

    public companion object : KoinRoute<Phone>() {

        override val navRoute: KClass<out NavRoute>
            get() = Phone::class

        @Composable
        override fun Content(
            route: Phone,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val inspectionMode = LocalInspectionMode.current
            val config = LocalConfig.current
            val router = currentRouter()
            val viewModel: PhoneViewModel = koinViewModel { parametersOf(config.auth.otp, route, router) }
            val state by viewModel.state.collectAsStateWithLifecycle()
            val country = remember(state.phone.dial) {
                Country.getCountries().find { country -> country.dial == state.phone.dial }
                    ?: (if (!inspectionMode) Country.current else null) ?: Country.forCode("US")
            }

            PhoneScreen(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                route,
                country,
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("email")
public data class Email(val username: String? = null) : NavRoute {

    override val route: Route<out NavRoute>
        get() = Email

    public companion object : KoinRoute<Email>() {

        override val navRoute: KClass<out NavRoute>
            get() = Email::class

        @Composable
        override fun Content(
            route: Email,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val config = LocalConfig.current
            val router = currentRouter()
            val viewModel: EmailViewModel = koinViewModel { parametersOf(config.auth.otp, route, router) }
            val state by viewModel.state.collectAsStateWithLifecycle()

            EmailScreen(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                route,
                config.validators[""]?.get("email") ?: Validator.email(),
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("hotp")
public data class Hotp(val username: String? = null, val contact: String) : NavRoute {

    override val route: Route<out NavRoute>
        get() = Hotp

    public companion object : KoinRoute<Hotp>() {

        override val navRoute: KClass<out NavRoute>
            get() = Hotp::class

        @Composable
        override fun Content(
            route: Hotp,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val config = LocalConfig.current
            val viewModel: HotpViewModel = koinViewModel { parametersOf(config.auth.otp, route) }
            val state by viewModel.state.collectAsStateWithLifecycle()
            val router = currentRouter()

            HotpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                config.auth.otp as HotpConfig,
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("totp")
public data class Totp(val username: String? = null, val contact: String) : NavRoute {

    override val route: Route<out NavRoute>
        get() = Totp

    public companion object : KoinRoute<Totp>() {

        override val navRoute: KClass<out NavRoute>
            get() = Totp::class

        @Composable
        override fun Content(
            route: Totp,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val config = LocalConfig.current
            val viewModel: TotpViewModel = koinViewModel { parametersOf(config.auth.otp, route) }
            val state by viewModel.state.collectAsStateWithLifecycle()
            val router = currentRouter()

            TotpScreen(
                Modifier.fillMaxSize().padding(horizontal = 16.dp),
                route,
                config.auth.otp as TotpConfig,
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("pin_code")
public data object PinCode : KoinRoute<PinCode>(), NavRoute {

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
@SerialName("reset_pin_code")
public data object ResetPinCode : KoinRoute<ResetPinCode>(), NavRoute {

    @Composable
    override fun Content(
        route: ResetPinCode,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        ResetPinCodeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("login")
public data object Login : KoinRoute<Login>(), NavRoute {

    @Composable
    override fun Content(
        route: Login,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val config = LocalConfig.current
        val router = currentRouter()
        val viewModel: LoginViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LoginScreen(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            route,
            config.auth,
            state,
            viewModel::action,
            router::actions,
        )
    }
}

@Serializable
@SerialName("home")
public data object Home : KoinRoute<Home>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    override val routes: List<BaseRoute> by lazy {
        listOf(Articles, ArticleDetails)
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
            TopAppBarSceneStrategy().delegate() +
                NavSuiteSceneStrategy().delegate() +
                TopAppBarNavSuiteSceneStrategy().delegate() +
                NavSuiteTopAppBarSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("articles")
public data object Articles : KoinRoute<Articles>(), NavRoute {

    @Composable
    override fun Content(
        route: Articles,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()
        val viewModel: ArticleViewModel = koinViewModel { parametersOf(router) }
        val state by viewModel.state.collectAsStateWithLifecycle()

        ArticlesScreen(
            Modifier,
            route,
            state,
            viewModel::action,
            router::actions,
        )
    }
}

@Serializable
@SerialName("article_details")
public data class ArticleDetails(val articleId: Long) : NavRoute {

    override val route: Route<out NavRoute>
        get() = ArticleDetails

    public companion object : KoinRoute<ArticleDetails>(), NavRoute {

        override val navRoute: KClass<out NavRoute>
            get() = ArticleDetails::class

        @Composable
        override fun Content(
            route: ArticleDetails,
            sharedTransitionScope: SharedTransitionScope,
        ) {
            val uriHandler = LocalUriHandler.current
            val share = LocalShare.current
            val scrollState = rememberScrollState()
            val router = currentRouter()
            val viewModel: ArticleDetailsViewModel = koinViewModel { parametersOf(uriHandler, share) }
            val state by viewModel.state.collectAsStateWithLifecycle()

            ArticleDetailsScreen(
                Modifier.fillMaxSize().verticalScroll(scrollState),
                route,
                state,
                viewModel::action,
                router::actions,
            )
        }
    }
}

@Serializable
@SerialName("map")
public data object Map : KoinRoute<Map>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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
@SerialName("services")
public data object Services : KoinRoute<Services>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

@Serializable()
@SerialName("wallet")
public data object Wallet : KoinRoutes() {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.Wallet, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.Wallet, text) },
            ),
        )
    }

    override val routes: List<BaseRoute> by lazy {
        listOf(Balance, Crypto, Exchange)
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
            TopAppBarSceneStrategy().delegate() +
                NavSuiteSceneStrategy().delegate() +
                TopAppBarNavSuiteSceneStrategy().delegate() +
                NavSuiteTopAppBarSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("balance")
public data object Balance : KoinRoute<Balance>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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
@SerialName("exchange")
public data object Exchange : KoinRoute<Exchange>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    @Composable
    override fun Content(
        route: Exchange,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        ExchangeScreen(
            Modifier,
            route,
            router::actions,
        )
    }
}

@Serializable
@SerialName("profile")
public data object Profile : KoinRoute<Profile>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    @Composable
    override fun Content(
        route: Profile,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val config = LocalConfig.current
        val connectivityStatus = LocalConnectivityStatus.current
        val componentsState = LocalConnectivityState.current
        val authState = LocalAuthState.current
        val viewModel: ProfileViewModel = koinViewModel { parametersOf(authState.value.user) }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val router = currentRouter()

        ProfileScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            connectivityStatus,
            componentsState.value,
            config.validators["user"].orEmpty(),
            state,
            viewModel::action,
            router::actions,
        )
    }

    @Composable
    override fun isNavigationItem(auth: Auth): Boolean {
        val isAvatar = LocalAppBarState.current.value.isAvatar
        return super.isNavigationItem(auth) && !isAvatar
    }
}

@Serializable
@SerialName("verification")
public data object Verification : KoinRoute<Verification>(), NavRoute {

    @Composable
    override fun Content(
        route: Verification,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val authState = LocalAuthState.current
        val router = currentRouter()
        val viewModel: VerificationViewModel = koinViewModel { parametersOf(router) }
        val state by viewModel.state.collectAsStateWithLifecycle()

        VerificationScreen(
            Modifier.fillMaxSize().padding(16.dp),
            route,
            state,
            viewModel::action,
            authState.value,
            { auth -> authState.value = auth },
            router::actions,
        )
    }
}

@Serializable
@SerialName("about")
public data object About : KoinRoute<About>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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
@SerialName("settings")
public data object Settings : KoinRoutes() {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
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

    override val routes: List<BaseRoute> by lazy {
        listOf(
            SettingsMain,
            SettingsColorScheme,
            SettingsDynamicColorScheme,
            SettingsShapes,
            SettingsTypography,
            SettingsRoute,
        )
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
            TopAppBarSceneStrategy().delegate() +
                NavSuiteSceneStrategy().delegate() +
                TopAppBarNavSuiteSceneStrategy().delegate() +
                NavSuiteTopAppBarSceneStrategy().delegate(),
        ),
        entryProvider = entryProvider,
    )
}

@Serializable
@SerialName("settings_main")
public data object SettingsMain : KoinRoute<SettingsMain>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsMain,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val config = LocalConfig.current
        val connectivityStatus = LocalConnectivityStatus.current
        val appBarState = LocalAppBarState.current
        val connectivityState = LocalConnectivityState.current
        val themeState = LocalThemeState.current
        val densityState = LocalDensityState.current
        val localeState = LocalLocaleState.current
        val permissionsState = LocalPermissionsState.current
        val routesState = LocalRoutesState.current
        val router = currentRouter()

        SettingsMainScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            connectivityStatus,
            config.ui.appBar,
            appBarState.value,
            { value -> appBarState.value = value },
            config.ui.connectivity,
            connectivityState.value,
            { value -> connectivityState.value = value },
            config.ui.theme,
            themeState.value,
            { value -> themeState.value = value },
            config.ui.density,
            densityState.value,
            { value -> densityState.value = value },
            config.localization.locales,
            config.localization.locale,
            localeState.value.inspectionModeAware,
            { value -> localeState.value = value },
            config.ui.routes,
            routesState.value,
            { route, value -> routesState[route] = value },
            permissionsState.permissions,
            { value ->
                coroutineScope.launch {
                    permissionsState.providePermission(value)
                }
            },
            permissionsState::openAppSettings,
            router::actions,
        )
    }
}

@Serializable
@SerialName("settings_color_scheme")
public data object SettingsColorScheme : KoinRoute<SettingsColorScheme>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsColorScheme,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val themeState = LocalThemeState.current

        SettingsColorSchemeScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            themeState.value,
        ) { value -> themeState.value = value }
    }
}

@Serializable
@SerialName("settings_dynamic_color_scheme")
public data object SettingsDynamicColorScheme : KoinRoute<SettingsDynamicColorScheme>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsDynamicColorScheme,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val themeState = LocalThemeState.current

        SettingsDynamicColorSchemeScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            themeState.value,
        ) { value -> themeState.value = value }
    }
}

@Serializable
@SerialName("settings_shapes")
public data object SettingsShapes : KoinRoute<SettingsShapes>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsShapes,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val themeState = LocalThemeState.current

        SettingsShapesScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            themeState.value,
        ) { value -> themeState.value = value }
    }
}

@Serializable
@SerialName("settings_typography")
public data object SettingsTypography : KoinRoute<SettingsTypography>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsTypography,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val fontStyles = remember { listOf(Normal, Italic) }
        val fontSynthesis = remember { listOf(None, Weight, Style, All) }
        val fontFamilies = remember {
            listOf(
                FontFamily.Default,
                FontFamily.SansSerif,
                FontFamily.Serif,
                FontFamily.Monospace,
                FontFamily.Cursive,
            )
        }
        val themeState = LocalThemeState.current

        SettingsTypographyScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            fontStyles,
            fontSynthesis,
            fontFamilies,
            themeState.value,
        ) { value -> themeState.value = value }
    }
}

@Serializable
@SerialName("settings_route")
public data object SettingsRoute : KoinRoute<SettingsRoute>(), NavRoute {

    @Composable
    override fun Content(
        route: SettingsRoute,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val scrollState = rememberScrollState()
        val routesState = LocalRoutesState.current

        SettingsRouteScreen(
            Modifier.fillMaxSize().padding(horizontal = 16.dp).verticalScroll(scrollState),
            route,
            routesState.value,
        ) { route, value -> routesState[route] = value }
    }
}

@Serializable
@SerialName("support")
public data object Support : KoinRoute<Support>(), NavRoute {

    override val selectableItem: @Composable (name: String) -> SelectableItem = { name ->
        val text = name.asStringResource()
        SelectableItem(
            item = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Outlined.SupportAgent, text) },
            ),
            selectedItem = Item(
                text = { Text(text) },
                icon = { Icon(Icons.Filled.SupportAgent, text) },
            ),
        )
    }

    @Composable
    override fun Content(
        route: Support,
        sharedTransitionScope: SharedTransitionScope,
    ) {
        val router = currentRouter()

        SupportScreen(
            Modifier,
            route,
            router::actions,
        )
    }

    @Composable
    override fun isNavigationItem(auth: Auth): Boolean {
        val isSupport = LocalAppBarState.current.value.isSupport
        return super.isNavigationItem(auth) && !isSupport
    }
}

