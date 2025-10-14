@file:OptIn(InternalAPI::class)

package klib.data.crud.http

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.InternalAPI
import klib.data.AggregateExpression
import klib.data.BooleanVariable
import klib.data.Variable
import klib.data.crud.CRUDRepository
import klib.data.crud.http.model.HttpCrud
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.net.http.client.KtorfitClient
import klib.data.net.http.client.bodyAsFlow
import klib.data.net.http.client.decodeFromAny
import klib.data.transaction.Transaction
import klib.data.transaction.TransactionContext
import klib.data.transaction.currentTransactionId
import kotlin.concurrent.atomics.AtomicBoolean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi

public open class CRUDClient<T : Any>(
    public val typeInfo: TypeInfo,
    baseUrl: String,
    httpClient: HttpClient,
) : KtorfitClient(baseUrl, httpClient), CRUDRepository<T> {

    private val api: CRUDApi = ktorfit.createCRUDApi()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <R> transactional(block: suspend CRUDRepository<T>.(Transaction) -> R): R {
        val transactionId = api.beginTransaction()
        val context = TransactionContext(transactionId)
        val completed = AtomicBoolean(false)

        return try {
            withContext(context) {
                block(
                    object : Transaction {
                        override suspend fun commit() {
                            if (completed.compareAndSet(expectedValue = false, newValue = true))
                                api.commitTransaction(transactionId)
                        }

                        override suspend fun rollback() {
                            if (completed.compareAndSet(expectedValue = false, newValue = true))
                                api.rollbackTransaction(transactionId)
                        }
                    },
                )
            }.also {
                if (completed.compareAndSet(expectedValue = false, newValue = true))
                    api.commitTransaction(transactionId)
            }
        }
        catch (e: Throwable) {
            if (completed.compareAndSet(expectedValue = false, newValue = true))
                api.rollbackTransaction(transactionId)
            throw e
        }
    }

    override suspend fun insert(entities: List<T>): Unit =
        api.insert(HttpCrud.Insert(currentTransactionId(), entities))

    override suspend fun insertAndReturn(entities: List<T>): List<T> =
        api.insertAndReturn(HttpCrud.InsertAndReturn(currentTransactionId(), entities))
            .execute(HttpResponse::body)

    override suspend fun update(entities: List<T>): List<Boolean> =
        api.update(HttpCrud.Update(currentTransactionId(), entities))

    override suspend fun update(projections: List<Map<String, Any?>>, predicate: BooleanVariable?): Long =
        api.updateProjections(
            HttpCrud.UpdateProjections(
                currentTransactionId(),
                projections,
                predicate,
            ),
        )

    override suspend fun upsert(entities: List<T>): List<T> =
        api.upsert(HttpCrud.Upsert(currentTransactionId(), entities))
            .execute(HttpResponse::body)

    override fun find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?): Flow<T> =
        flow {
            emitAll(
                api.find(
                    HttpCrud.Find(currentTransactionId(), sort, predicate, limitOffset),
                ).execute().bodyAsFlow(typeInfo),
            )
        }

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    override fun find(
        projections: List<Variable>,
        sort: List<Order>?,
        predicate: BooleanVariable?,
        limitOffset: LimitOffset?,
    ): Flow<List<Any?>> = flow {
        emitAll(
            api.findProjections(
                HttpCrud.FindProjections(
                    currentTransactionId(),
                    projections,
                    sort,
                    predicate,
                    limitOffset,
                ),
            ).execute().bodyAsFlow() as Flow<List<Any?>>,
        )
    }

    override suspend fun delete(predicate: BooleanVariable?): Long =
        api.delete(HttpCrud.Delete(currentTransactionId(), predicate))

    override suspend fun <T> aggregate(aggregate: AggregateExpression<T>, predicate: BooleanVariable?): T =
        api.aggregate(HttpCrud.Aggregate(currentTransactionId(), aggregate, predicate))
            .execute().decodeFromAny()
}
