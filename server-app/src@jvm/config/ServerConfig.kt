@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package config

import io.ktor.server.application.Application
import io.ktor.server.application.ServerConfig
import io.ktor.server.engine.ApplicationEngine;
import io.ktor.server.engine.ApplicationEnvironmentBuilder
import io.ktor.util.PlatformUtils
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import io.ktor.server.engine.WORKING_DIRECTORY_PATH

public class ServerConfig<TConfiguration : ApplicationEngine.Configuration>(
    public val engine: TConfiguration
) {

    public val environment: ApplicationEnvironmentBuilder = ApplicationEnvironmentBuilder()

    internal val modules: MutableList<suspend Application.() -> Unit> = mutableListOf()

    /**
     * Paths to wait for application reload.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.server.application.ServerConfigBuilder.watchPaths)
     */
    public var watchPaths: List<String> = listOf(WORKING_DIRECTORY_PATH)

    /**
     * Application's root path (prefix, context path in servlet container).
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.server.application.ServerConfigBuilder.rootPath)
     */
    public var rootPath: String = ""

    /**
     * Indicates whether development mode is enabled.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.server.application.ServerConfigBuilder.developmentMode)
     */
    public var developmentMode: Boolean = PlatformUtils.IS_DEVELOPMENT_MODE

    /**
     * Parent coroutine context for an application.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.server.application.ServerConfigBuilder.parentCoroutineContext)
     */
    public var parentCoroutineContext: CoroutineContext = EmptyCoroutineContext

    /**
     * Installs an application module.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.server.application.ServerConfigBuilder.module)
     */
    public fun module(body: suspend Application.() -> Unit) {
        modules.add(body)
    }

    public fun config(): ServerConfig =
        ServerConfig(environment.build(), modules.toList(), watchPaths, rootPath, developmentMode, parentCoroutineContext)
}
