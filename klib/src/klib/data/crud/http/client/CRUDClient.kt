@file:OptIn(InternalAPI::class)

package klib.data.crud.http.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.InternalAPI
import klib.data.AggregateExpression
import klib.data.BooleanVariable
import klib.data.Variable
import klib.data.crud.CRUDRepository
import klib.data.crud.http.createCRUDApi
import klib.data.crud.http.model.HttpCrud
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.net.http.client.KtorfitClient
import klib.data.net.http.client.bodyAsAnyFlow
import klib.data.net.http.client.bodyAsFlow
import klib.data.net.http.client.bodyAsPolymorphic
import klib.data.transaction.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

public class CRUDClient<T : Any>(
    baseUrl: String,
    httpClient: HttpClient,
    private val typeInfo: TypeInfo,
) : KtorfitClient(baseUrl, httpClient), CRUDRepository<T> {

    private val api: CRUDApi = ktorfit.createCRUDApi()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <R> transactional(block: suspend CRUDRepository<T>.(Transaction) -> R): R {
        error("Remote transaction is not supported")
    }

    override suspend fun insert(entities: List<T>): Unit =
        api.insert(HttpCrud.Insert(entities))

    override suspend fun insertAndReturn(entities: List<T>): List<T> =
        api.insertAndReturn(HttpCrud.InsertAndReturn(entities))
            .execute(HttpResponse::body)

    override suspend fun update(entities: List<T>): List<Boolean> =
        api.update(HttpCrud.Update(entities))

    override suspend fun update(properties: List<Map<String, Any?>>, predicate: BooleanVariable?): Long =
        api.updateProperties(
            HttpCrud.UpdateProperties(properties, predicate),
        )

    override suspend fun upsert(entities: List<T>): List<T> =
        api.upsert(HttpCrud.Upsert(entities))
            .execute(HttpResponse::body)

    override fun find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?): Flow<T> =
        flow {
            emitAll(
                api.find(HttpCrud.Find(sort, predicate, limitOffset))
                    .execute().bodyAsFlow(typeInfo = typeInfo),
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun find(
        projections: List<Variable>,
        sort: List<Order>?,
        predicate: BooleanVariable?,
        limitOffset: LimitOffset?,
    ): Flow<List<Any?>> = flow {
        emitAll(
            api.findProperties(
                HttpCrud.FindProperties(
                    projections,
                    sort,
                    predicate,
                    limitOffset,
                ),
            ).execute().bodyAsAnyFlow() as Flow<List<Any?>>,
        )
    }

    override suspend fun delete(predicate: BooleanVariable?): Long =
        api.delete(HttpCrud.Delete(predicate))

    override suspend fun <T> aggregate(aggregate: AggregateExpression<T>, predicate: BooleanVariable?): T =
        api.aggregate(HttpCrud.Aggregate(aggregate, predicate)).execute().bodyAsPolymorphic { null }
}
