import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import config.ApplicationScript
import config.ServerConfig
import engine.Netty
import io.github.smiley4.ktoropenapi.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.yaml.YamlConfig
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlinx.coroutines.awaitCancellation

public fun main(): Unit = SuspendApp {
    resourceScope {
        val engineConfig = NettyApplicationEngine.Configuration()

        val serverConfig = ServerConfig().apply {
            module(Application::module)
        }

        ApplicationScript(engineConfig, serverConfig)()

        server(
            Netty(engineConfig),
            rootConfig = serverConfig(),
        )

        awaitCancellation()
    }
}

@Suppress("unused")
private fun Application.module() {
}

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


