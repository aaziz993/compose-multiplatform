import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import config.ApplicationScript
import config.ServerConfig
import engine.Netty
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlinx.coroutines.awaitCancellation

public fun main(): Unit = SuspendApp {
    resourceScope {
        val applicationFile = File(
            {}.javaClass.classLoader.getResource("application.yaml")?.toURI()
                ?: error("application.yaml not found in resources"),
        )

        val engineConfig = NettyApplicationEngine.Configuration()

        val serverConfig = ServerConfig().apply {
            module(Application::module)
        }

        ApplicationScript(applicationFile, engineConfig, serverConfig)()

        server(
            Netty(engineConfig),
            rootConfig = serverConfig.config(),
        )

        awaitCancellation()
    }
}

@Suppress("unused")
public fun Application.module() {
    install(SSE)
}

public fun Application.ping(): Routing = routing {
    get("/ping") { call.respondText("pong") }
}


