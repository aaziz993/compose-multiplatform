import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.awaitCancellation

public fun main(args: Array<String>): Unit = SuspendApp {
    resourceScope {
        server(Netty, watchPaths = emptyList()) {
            routing {
                get("/ping") {
                    call.respond("pong")
                }
            }
        }

        awaitCancellation()
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


