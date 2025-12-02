package di

import io.ktor.server.application.Application
import io.ktor.server.application.install
import klib.data.config.Config
import klib.data.config.EnabledConfig
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

public fun Application.configureKoin(
    config: Config,
    application: KoinApplication.() -> Unit = {}
) {
    install(Koin) {
        config.koin.takeIf(EnabledConfig::enabled)?.let { koin ->
            koin.logging?.takeIf(EnabledConfig::enabled)?.level?.let { slf4jLogger(Level.valueOf(it)) } ?: slf4jLogger()
        }

        modules(
            module { single { config } },
        )

        application()
    }
}




