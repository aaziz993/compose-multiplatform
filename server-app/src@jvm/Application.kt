import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import config.ApplicationScript
import config.ServerConfig
import engine.Netty
import io.github.smiley4.ktoropenapi.get
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.time.format.*
import java.time.temporal.*
import java.util.*
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
//    install(ConditionalHeaders) {
//        val file = File("src@jvm/Application.kt")
//        version { call, outgoingContent ->
//            when (outgoingContent.contentType?.withoutParameters()) {
//                ContentType.Text.CSS -> listOf(
//                    EntityTagVersion(file.lastModified().hashCode().toString()),
//                    LastModifiedVersion(Date(file.lastModified()))
//                )
//                else -> emptyList()
//            }
//        }
//    }
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


