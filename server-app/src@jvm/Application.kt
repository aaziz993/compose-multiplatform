import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import config.ApplicationScript
import config.ServerConfig
import engine.Netty
import io.github.smiley4.ktoropenapi.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing
import kotlinx.coroutines.awaitCancellation

public fun main(): Unit = SuspendApp {
    resourceScope {
        val engineConfig = NettyApplicationEngine.Configuration()

        val serverConfig = ServerConfig()

        ApplicationScript(engineConfig, serverConfig)()

        serverConfig.apply {
            module(Application::module)
        }

        server(
            Netty(engineConfig),
            rootConfig = serverConfig(),
        )

        awaitCancellation()
    }
}

/**
 * Additional module configurations maybe done here.
 */
private fun Application.module() = Unit

public fun Application.ping(): Routing = routing {
    get(
        "/ping",
        {
            description = "A Ktor Server test."
            response {
                HttpStatusCode.OK to {
                    description = "A success response"
                    body<String>()
                }
            }
        },
    ) { call.respondText("pong") }
}


