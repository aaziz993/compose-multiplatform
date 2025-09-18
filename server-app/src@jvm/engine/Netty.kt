package engine

import io.ktor.events.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

public class Netty(private val configuration: NettyApplicationEngine.Configuration)
    : ApplicationEngineFactory<NettyApplicationEngine, NettyApplicationEngine.Configuration> {

    override fun configuration(
        configure: NettyApplicationEngine.Configuration.() -> Unit
    ): NettyApplicationEngine.Configuration = configuration.apply(configure)

    override fun create(
        environment: ApplicationEnvironment,
        monitor: Events,
        developmentMode: Boolean,
        configuration: NettyApplicationEngine.Configuration,
        applicationProvider: () -> Application
    ): NettyApplicationEngine =
        NettyApplicationEngine(environment, monitor, developmentMode, configuration, applicationProvider)
}
