import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import io.ktor.server.application.*
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.awaitCancellation

public fun main(): Unit = SuspendApp {
    resourceScope {
        server(Netty) {
            ping()
            indexHtml()
        }

        awaitCancellation()
    }
}

public fun Application.ping(): Routing = routing {
    get("/ping") { call.respondText("pong") }
}

public fun Application.indexHtml(): Routing = routing {
    staticResources("/", "static") {
        default("static/index.html")
    }
}

//@Suppress("unused")
//public fun Application.module(config: ServerConfigImpl) = configure(
//    config,
//    { ServerModule().module },
//    routingBlock = {
//        // Add all other routes here
//        mapRouting(get())
//    },
//)


