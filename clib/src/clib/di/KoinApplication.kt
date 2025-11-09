package clib.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.currentComposer
import org.koin.compose.ComposeContextWrapper
import org.koin.compose.application.rememberKoinApplication
import org.koin.compose.application.rememberKoinMPApplication
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.logger.Level
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinApplication
import org.koin.mp.KoinPlatform

/**
 * Internal API
 * Current Koin Scope, as default with Default Koin context root scope
 *
 * @see ComposeContextWrapper
 */
//TODO later make it internal
@KoinInternalApi
public val LocalKoinScopeContext: ProvidableCompositionLocal<ComposeContextWrapper<Scope>> = compositionLocalOf { ComposeContextWrapper(getDefaultRootScope()) { getDefaultRootScope() } }

/**
 * Internal API
 * Current Koin Application context, as default with Default Koin context
 *
 * @see ComposeContextWrapper
 */
@KoinInternalApi
internal val LocalKoinApplicationContext: ProvidableCompositionLocal<ComposeContextWrapper<Koin>> = compositionLocalOf { ComposeContextWrapper(getDefaultKoinContext()) { getDefaultKoinContext() } }

private fun getDefaultKoinContext() = KoinPlatform.getKoin()

@OptIn(KoinInternalApi::class)
private fun getDefaultRootScope() = KoinPlatform.getKoin().scopeRegistry.rootScope

/**
 * Retrieve the current Koin application from the composition.
 *
 * @author @author jjkester
 */
@OptIn(InternalComposeApi::class, KoinInternalApi::class)
@Composable
public fun getKoin(): Koin = currentComposer.run {
    try {
        consume(LocalKoinApplicationContext).getValue()
    } catch (e: Exception) {
        consume(LocalKoinApplicationContext).resetValue()
            ?: error("Can't get Koin context due to error: $e")
    }
}

/**
 * Retrieve the current Koin scope from the composition
 *
 * @author @author jjkester
 *
 */
@OptIn(InternalComposeApi::class, KoinInternalApi::class)
@Composable
public fun currentKoinScope(): Scope = currentComposer.run {
    try {
        val currentScope = consume(LocalKoinScopeContext).getValue()
        if (currentScope.closed) consume(LocalKoinScopeContext).resetValue() ?: error("Can't get Koin scope. Scope '$currentScope' is closed")
        else currentScope
    } catch (e: Exception) {
        consume(LocalKoinScopeContext).resetValue()
            ?: error("Can't get Koin scope due to error: $e")
    }
}

/**
 * Start a new Koin Application context and setup Compose context
 * if Koin's Default Context is already set, throw an error
 *
 * @param application - Koin Application declaration lambda
 * @param content - following compose function
 *
 * @throws org.koin.core.error.KoinApplicationAlreadyStartedException
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinInternalApi::class)
@Composable
public fun KoinApplication(
    application: KoinAppDeclaration, //Better to directly use KoinConfiguration class
    content: @Composable () -> Unit
) {
    val koin = rememberKoinApplication(application)
    CompositionLocalProvider(
        LocalKoinApplicationContext provides ComposeContextWrapper(koin) { getDefaultKoinContext() },
        LocalKoinScopeContext provides ComposeContextWrapper(koin.scopeRegistry.rootScope) { getDefaultRootScope() },
        content = content
    )
}

/**
 * Start a new Koin Application context, configure default context binding (android) & logger, setup Compose context
 * if Koin's Default Context is already set, throw an error
 *
 * Call composeMultiplatformConfiguration to help prepare/anticipate context setup, and avoid to have different configuration in KMP app
 * this function takes care to setup Android context (androidContext, androidLogger) for you
 * @see composeMultiplatformConfiguration()
 *
 * @param config - Koin Application Configuration (use koinConfiguration { } to declare your Koin application)
 * @see KoinConfiguration
 *
 * @param logLevel - KMP active logger (androidLogger or printLogger)
 * @param content - following compose function
 *
 * @throws org.koin.core.error.KoinApplicationAlreadyStartedException
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinInternalApi::class)
@Composable
@KoinExperimentalAPI
public fun KoinMultiplatformApplication(
    config: KoinConfiguration,
    logLevel: Level = Level.INFO,
    content: @Composable () -> Unit
) {
    val koin = rememberKoinMPApplication(config, logLevel)
    CompositionLocalProvider(
        LocalKoinApplicationContext provides ComposeContextWrapper(koin) { getDefaultKoinContext() },
        LocalKoinScopeContext provides ComposeContextWrapper(koin.scopeRegistry.rootScope) { getDefaultRootScope() },
        content = content
    )
}

/**
 * Handle Multiplatform Config & Logger level
 * - Help handle automatically Android Logger Anticipate Android context injection, to having to setup androidContext() and androidLogger
 */
//@Composable
//@PublishedApi
//@KoinInternalApi
//internal expect fun composeMultiplatformConfiguration(
//    loggerLevel: Level = Level.INFO,
//    config: KoinConfiguration
//): KoinConfiguration

/**
 * Provides Koin Isolated context to be setup into LocalKoinApplication & LocalKoinScope via CompositionLocalProvider,
 * to be used by child Composable.
 *
 * This allows to use an isolated context, directly in all current Composable API
 *
 * Koin isolated context has to created with koinApplication() function, storing the instance in a static field
 *
 * @param context - Koin isolated context
 * @param content - child Composable
 *
 * @author Arnaud Giuliani
 */
@OptIn(KoinInternalApi::class)
@Composable
public fun KoinIsolatedContext(
    context: KoinApplication,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalKoinApplicationContext provides ComposeContextWrapper(context.koin) { context.koin} ,
        LocalKoinScopeContext provides ComposeContextWrapper(context.koin.scopeRegistry.rootScope) { context.koin.scopeRegistry.rootScope } ,
        content = content
    )
}

/**
 * Composable Function to run a local Koin application and to help run Compose preview
 * This function is lighter than KoinApplication, and allow parallel recomposition in Android Studio
 *
 * @param application - Koin application config
 * @param content
 */
@OptIn(KoinInternalApi::class)
@Composable
public fun KoinApplicationPreview(
    application: KoinAppDeclaration,
    content: @Composable () -> Unit
) {
    val context = koinApplication(application)
    CompositionLocalProvider(
        LocalKoinApplicationContext provides ComposeContextWrapper(context.koin) {context.koin},
        LocalKoinScopeContext provides ComposeContextWrapper(context.koin.scopeRegistry.rootScope) {context.koin.scopeRegistry.rootScope},
        content = content
    )
}
