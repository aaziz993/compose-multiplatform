package klib.data.crud.http

import io.ktor.http.*
import io.ktor.http.content.OutputStreamContent
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.OutputStream
import klib.data.crud.CRUDRepository
import klib.data.crud.http.model.HttpCrud
import klib.data.cryptography.encodeDerToByteArray
import klib.data.type.collections.writeToChannel
import klib.data.type.collections.writeToOutputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json

@OptIn(InternalSerializationApi::class)
@Suppress("FunctionName", "UNCHECKED_CAST")
public inline fun <reified T : Any> Routing.CrudRoutes(
    baseUrl: String,
    repository: CRUDRepository<T>,
) {
    route(baseUrl) {
        post("transaction") {

            call.bod.body().filterNotNull().collect {
                with(Json.Default.decodeFromString<HttpOperation>(it)) {
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
            repository.insert(call.receive<HttpCrud.Insert<T>>().values)
            call.respond(HttpStatusCode.OK, "Successful")
        }

        put("insertAndReturn") {
            call.respond(HttpStatusCode.OK, repository.insertAndReturn(call.receive<HttpCrud.InsertAndReturn<T>>().values))
        }

        post("update") {
            call.respond(HttpStatusCode.OK, repository.update(call.receive<HttpCrud.Update<T>>().values))
        }

        post("updateProjections") {
            with(call.receive<HttpCrud.UpdateProjections>()) {
                call.respond(HttpStatusCode.OK, repository.update(values, predicate))
            }
        }

        put("upsert") {
            call.respond(HttpStatusCode.OK, repository.upsert(call.receive<HttpCrud.Upsert<T>>().values))
        }

        post("delete") {
            call.respond(HttpStatusCode.OK, repository.delete(call.receive<HttpCrud.Delete>().predicate))
        }

        post("find") {
            with(call.receive<HttpCrud.Find>()) {
                call.respondOutputStream(status = HttpStatusCode.OK)
            }
        }
        post("findProjections") {
            with(call.receive<HttpCrud.FindProjections>()) {
                call.respondOutputStream(status = HttpStatusCode.OK, flow = repository.find(projections, sort, predicate, limitOffset).map(Json.Default::encodeAnyToString))
            }
        }

        post("aggregate") {
            with(call.receive<HttpCrud.Aggregate>()) {
                repository.aggregate(aggregate, predicate)?.let {
                    call.respond(
                        HttpStatusCode.OK,
                        Json.Default.encodeToString(PolymorphicSerializer(Any::class), it),
                    )
                } ?: call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
