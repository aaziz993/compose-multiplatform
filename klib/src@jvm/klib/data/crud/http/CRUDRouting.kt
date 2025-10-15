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
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json

@OptIn(InternalSerializationApi::class)
@Suppress("FunctionName", "UNCHECKED_CAST")
public inline fun <reified T : Any> Routing.CrudRoutes(
    baseUrl: String,
    typeInfo: TypeInfo,
    repository: CRUDRepository<T>,
) {
    route(baseUrl) {
        post("transaction") {
            call.filterNotNull().collect {
                with(Json.Default.decodeFromString<HttpCrud>(it)) {
                    when (this) {
                        is HttpCrud.Insert<*> -> repository.insert(values as List<T>)
                        is HttpCrud.InsertAndReturn<*> -> repository.insertAndReturn(values as List<T>)
                        is HttpCrud.Update<*> -> repository.update(values as List<T>)
                        is HttpCrud.UpdateUntyped -> repository.update(propertyValues, predicate)
                        is HttpCrud.Upsert<*> -> repository.upsert(values as List<T>)

                        else -> Unit
                    }
                }
            }
        }

        put("insert") {
            with(call.receive<HttpCrud.Insert<T>>()) {
                repository.insert(values)
                call.respond(HttpStatusCode.Created, "Successful")
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

        post("updateProjections") {
            with(call.receive<HttpCrud.UpdateProjections>()) {
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
                call.respondFlow(status = HttpStatusCode.OK, typeInfo = typeInfo, flow = repository.find(sort, predicate, limitOffset))
            }
        }

        post("findProjections") {
            with(call.receive<HttpCrud.FindProjections>()) {
                call.respondAnyFlow(status = HttpStatusCode.OK, flow = repository.find(projections, sort, predicate, limitOffset))
            }
        }

        post("aggregate") {
            with(call.receive<HttpCrud.Aggregate>()) {
                repository.aggregate(aggregate, predicate)?.let { value ->
                    call.respond(
                        HttpStatusCode.OK,
                        Json.Default.encodeToString(PolymorphicSerializer(Any::class), value),
                    )
                } ?: call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
