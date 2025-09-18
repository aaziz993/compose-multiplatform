import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import io.ktor.server.application.*
import io.ktor.server.http.content.*
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

        val serverConfig = ServerConfig(NettyApplicationEngine.Configuration()).apply {
            ApplicationScript(applicationFile, this)()
            module(Application::module)
        }

        server(
            Netty(serverConfig.engine),
            rootConfig = serverConfig.toServerConfig(),
        )

        awaitCancellation()
    }
}

@Suppress("unused")
public fun Application.module() {
}

public fun Application.ping(): Routing = routing {
    get("/ping") { call.respondText("pong") }
}

public fun Application.indexHtml(): Routing = routing {
    staticResources("/", "static") {
        default("static/index.html")
    }
}
