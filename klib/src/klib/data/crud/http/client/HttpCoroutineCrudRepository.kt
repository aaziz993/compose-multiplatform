@file:OptIn(InternalAPI::class)

package klib.data.crud.http.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.InternalAPI
import klib.data.crud.CoroutineCrudRepository
import klib.data.crud.http.model.HttpCrud
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.bodyAsAnyFlow
import klib.data.net.http.client.bodyAsFlow
import klib.data.net.http.client.bodyAsPolymorphic
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.ktorfit
import klib.data.query.AggregateExpression
import klib.data.query.BooleanOperand
import klib.data.query.LimitOffset
import klib.data.query.Order
import klib.data.query.Variable
import klib.data.transaction.CoroutineTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

public class HttpCoroutineCrudRepository<T : Any>(
    private val typeInfo: TypeInfo,
    baseUrl: String,
    httpClient: HttpClient = createHttpClient {
        install(ContentNegotiation) {
            json(HTTP_CLIENT_JSON)
        }
    },
) : CoroutineCrudRepository<T> {

    private val api: CrudApi = httpClient.ktorfit { baseUrl(baseUrl) }.createCrudApi()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <R> transactional(block: suspend CoroutineCrudRepository<T>.(CoroutineTransaction) -> R): R {
        error("Remote transaction is not supported")
    }

    override fun find(predicate: BooleanOperand?, orderBy: List<Order>, limitOffset: LimitOffset?): Flow<T> =
        flow {
            emitAll(
                api.find(HttpCrud.Find(predicate as Variable?, orderBy, limitOffset))
                    .execute().bodyAsFlow(typeInfo = typeInfo),
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun find(
        properties: List<Variable>,
        predicate: BooleanOperand?,
        orderBy: List<Order>,
        limitOffset: LimitOffset?,
    ): Flow<List<Any?>> = flow {
        emitAll(
            api.findProperties(
                HttpCrud.FindProperties(
                    properties,
                    predicate as Variable?,
                    orderBy,
                    limitOffset,
                ),
            ).execute().bodyAsAnyFlow() as Flow<List<Any?>>,
        )
    }

    override fun observe(predicate: BooleanOperand?, orderBy: List<Order>, limitOffset: LimitOffset?): Flow<T> =
        flow {
            emitAll(
                api.observe(HttpCrud.Observe(predicate as Variable?, orderBy, limitOffset))
                    .execute().bodyAsFlow(typeInfo = typeInfo),
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun observe(
        properties: List<Variable>,
        predicate: BooleanOperand?,
        orderBy: List<Order>,
        limitOffset: LimitOffset?,
    ): Flow<List<Any?>> = flow {
        emitAll(
            api.observeProperties(
                HttpCrud.ObserveProperties(
                    properties,
                    predicate as Variable?,
                    orderBy,
                    limitOffset,
                ),
            ).execute().bodyAsAnyFlow() as Flow<List<Any?>>,
        )
    }

    override suspend fun delete(predicate: BooleanOperand?): Long =
        api.delete(HttpCrud.Delete(predicate as Variable))

    override suspend fun <T> aggregate(aggregate: AggregateExpression, predicate: BooleanOperand?): T =
        api.aggregate(HttpCrud.Aggregate(aggregate, predicate as Variable?)).execute().bodyAsPolymorphic { null }

    override suspend fun insert(entities: List<T>): List<T> =
        api.insert(HttpCrud.Insert(entities)).execute(HttpResponse::body)

    override suspend fun update(properties: Map<String, Any?>, predicate: BooleanOperand?): Long =
        api.update(HttpCrud.Update(properties, predicate as Variable?))

    override suspend fun update(entity: T): Boolean = api.updateEntity(HttpCrud.UpdateEntity(entity))

    override suspend fun upsert(entities: List<T>, predicate: BooleanOperand?): List<T> =
        api.upsert(HttpCrud.Upsert(entities, predicate as Variable?)).execute(HttpResponse::body)
}
