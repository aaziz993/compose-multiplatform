package klib.data.crud.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.util.reflect.typeInfo
import klib.data.crud.CrudRepository
import klib.data.crud.http.model.HttpCrud
import klib.data.network.http.server.respondAnyFlow
import klib.data.network.http.server.respondFlow
import klib.data.network.http.server.respondPolymorphic
import klib.data.query.BooleanOperand
import kotlinx.coroutines.flow.asFlow

context(routing: Routing)
public inline fun <reified T : Any> CrudRepository<T>.routes(baseUrl: String) {
    routing.route(baseUrl) {
        routing.post("find") {
            with(call.receive<HttpCrud.Find>()) {
                call.respondFlow(
                    status = HttpStatusCode.OK,
                    typeInfo = typeInfo<T>(),
                    flow = find(predicate as BooleanOperand?, orderBy, limitOffset).asFlow(),
                )
            }
        }

        routing.post("findProperties") {
            with(call.receive<HttpCrud.FindProperties>()) {
                call.respondAnyFlow(
                    status = HttpStatusCode.OK,
                    flow = find(properties, predicate as BooleanOperand?, orderBy, limitOffset).asFlow(),
                )
            }
        }

        routing.post("observe") {
            with(call.receive<HttpCrud.Find>()) {
                call.respondFlow(
                    status = HttpStatusCode.OK,
                    typeInfo = typeInfo<T>(),
                    flow = observe(predicate as BooleanOperand?, orderBy, limitOffset).asFlow(),
                )
            }
        }

        routing.post("observeProperties") {
            with(call.receive<HttpCrud.FindProperties>()) {
                call.respondAnyFlow(
                    status = HttpStatusCode.OK,
                    flow = observe(properties, predicate as BooleanOperand?, orderBy, limitOffset).asFlow(),
                )
            }
        }

        routing.post("aggregate") {
            with(call.receive<HttpCrud.Aggregate>()) {
                call.respondPolymorphic(aggregate(aggregate, predicate as BooleanOperand?))
            }
        }

        routing.put("insert") {
            with(call.receive<HttpCrud.Insert<T>>()) {
                call.respond(HttpStatusCode.Created, insert(entities))
            }
        }

        routing.post("update") {
            with(call.receive<HttpCrud.Update>()) {
                call.respond(
                    HttpStatusCode.OK,
                    update(properties, predicate as BooleanOperand?),
                )
            }
        }

        routing.post("updateEntity") {
            with(call.receive<HttpCrud.UpdateEntity<T>>()) {
                call.respond(HttpStatusCode.OK, update(entity))
            }
        }

        routing.put("upsert") {
            with(call.receive<HttpCrud.Upsert<T>>()) {
                call.respond(
                    HttpStatusCode.OK,
                    upsert(entities, predicate as BooleanOperand?),
                )
            }
        }

        routing.post("delete") {
            with(call.receive<HttpCrud.Delete>()) {
                call.respond(
                    HttpStatusCode.OK,
                    delete(predicate as BooleanOperand?),
                )
            }
        }
    }
}

