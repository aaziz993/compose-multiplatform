package klib.data.crud.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.util.reflect.TypeInfo
import klib.data.crud.CRUDRepository
import klib.data.crud.http.model.HttpCrud
import klib.data.net.http.server.respondAnyFlow
import klib.data.net.http.server.respondFlow
import klib.data.net.http.server.respondPolymorphic

public inline fun <reified T : Any> Routing.crudRoutes(
    baseUrl: String,
    typeInfo: TypeInfo,
    repository: CRUDRepository<T>,
) {
    route(baseUrl) {
        put("insert") {
            with(call.receive<HttpCrud.Insert<T>>()) {
                repository.insert(values)
                call.respond(HttpStatusCode.Created)
            }
        }

        put("insertAndReturn") {
            with(call.receive<HttpCrud.InsertAndReturn<T>>()) {
                call.respond(HttpStatusCode.Created, repository.insertAndReturn(values))
            }
        }

        post("update") {
            with(call.receive<HttpCrud.Update<T>>()) {
                call.respond(HttpStatusCode.OK, repository.update(values))
            }
        }

        post("updateProperties") {
            with(call.receive<HttpCrud.UpdateProperties>()) {
                call.respond(HttpStatusCode.OK, repository.update(values, predicate))
            }
        }

        put("upsert") {
            with(call.receive<HttpCrud.Upsert<T>>()) {
                call.respond(HttpStatusCode.OK, repository.upsert(values))
            }
        }

        post("delete") {
            with(call.receive<HttpCrud.Delete>()) {
                call.respond(HttpStatusCode.OK, repository.delete(predicate))
            }
        }

        post("find") {
            with(call.receive<HttpCrud.Find>()) {
                call.respondFlow(
                    status = HttpStatusCode.OK,
                    typeInfo = typeInfo,
                    flow = repository.find(sort, predicate, limitOffset),
                )
            }
        }

        post("findProperties") {
            with(call.receive<HttpCrud.FindProperties>()) {
                call.respondAnyFlow(
                    status = HttpStatusCode.OK,
                    flow = repository.find(projections, sort, predicate, limitOffset),
                )
            }
        }

        post("aggregate") {
            with(call.receive<HttpCrud.Aggregate>()) {
                call.respondPolymorphic(repository.aggregate(aggregate, predicate))
            }
        }
    }
}

