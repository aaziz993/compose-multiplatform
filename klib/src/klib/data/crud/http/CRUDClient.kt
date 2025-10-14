//@file:OptIn(InternalAPI::class)
//
//package klib.data.crud.http
//
//import io.ktor.client.*
//import io.ktor.client.call.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.http.content.*
//import io.ktor.util.reflect.TypeInfo
//import io.ktor.utils.io.*
//import klib.data.AggregateExpression
//import klib.data.BooleanVariable
//import klib.data.Variable
//import klib.data.crud.CRUDRepository
//import klib.data.crud.http.model.HttpCrud
//import klib.data.crud.model.query.LimitOffset
//import klib.data.crud.model.query.Order
//import klib.data.net.http.client.KtorfitClient
//import klib.data.net.http.client.bodyAsFlow
//import klib.data.transaction.Transaction
//import klib.data.type.collections.writeByteArrayWithLength
//import kotlinx.coroutines.CompletableDeferred
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.emitAll
//import kotlinx.coroutines.flow.flow
//import kotlinx.serialization.InternalSerializationApi
//import kotlinx.serialization.PolymorphicSerializer
//import kotlinx.serialization.json.Json
//
//public open class CRUDClient<T : Any>(
//    public val typeInfo: TypeInfo,
//    private val baseUrl: String,
//    httpClient: HttpClient,
//) : KtorfitClient(baseUrl, httpClient), CRUDRepository<T> {
//
//    private val api: CRUDApi = ktorfit.createCRUDApi()
//
//    override suspend fun <R> transactional(block: suspend CRUDRepository<T>.(Transaction) -> R): R  {
//        val result = CompletableDeferred<R>()
//
//        api.transaction {
//            body = HttpTransaction(
//                { transaction ->
//                    try {
//                        result.complete(block(transaction))
//                        transaction.commit()
//                    }
//                    catch (t: Throwable) {
//                        result.completeExceptionally(t)
//                        transaction.rollback()
//                    }
//                },
//                ContentType.Application.Json,
//            )
//        }
//
//        return result.await()
//    }
//
//    override suspend fun insert(entities: List<T>): Unit = api.insert(HttpCrud.Insert(entities))
//
//    override suspend fun insertAndReturn(entities: List<T>): List<T> =
//        api.insertAndReturn(HttpCrud.InsertAndReturn(entities)).execute(HttpResponse::body)
//
//    override suspend fun update(entities: List<T>): List<Boolean> =
//        api.update(HttpCrud.Update(entities))
//
//    override suspend fun update(projections: List<Map<String, Any?>>, predicate: BooleanVariable?): Long =
//        api.update(HttpCrud.UpdateProjection(projections, predicate))
//
//    override suspend fun upsert(entities: List<T>): List<T> =
//        api.upsert(HttpCrud.Upsert(entities)).execute(HttpResponse::body)
//
//    override fun find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?): Flow<T> =
//        flow {
//            emitAll(
//                api.find(
//                    HttpCrud.Find(null, sort, predicate, limitOffset),
//                ).execute().bodyAsFlow(typeInfo),
//            )
//        }
//
//    @Suppress("UNCHECKED_CAST")
//    @OptIn(InternalSerializationApi::class)
//    override fun find(
//        projections: List<Variable>,
//        sort: List<Order>?,
//        predicate: BooleanVariable?,
//        limitOffset: LimitOffset?,
//    ): Flow<List<Any?>> = flow {
//        emitAll(
//            api.find(
//                HttpCrud.Find(projections, sort, predicate, limitOffset),
//            ).execute().bodyAsFlow() as Flow<List<Any?>>,
//        )
//    }
//
//    override suspend fun delete(predicate: BooleanVariable?): Long = api.delete(HttpCrud.Delete(predicate))
//
//    @Suppress("UNCHECKED_CAST")
//    override suspend fun <T> aggregate(aggregate: AggregateExpression<T>, predicate: BooleanVariable?): T =
//        api.aggregate(
//            HttpCrud.Aggregate(aggregate, predicate),
//        ).execute().takeIf { it.status != HttpStatusCode.NoContent }?.let {
//            json.decodeFromString(PolymorphicSerializer(Any::class), it.bodyAsText())
//        } as T
//}
//
//public class HttpTransaction<T : Any>(
//    typeInfo: TypeInfo,
//    baseUrl: String,
//    httpClient: HttpClient,
//) : Transaction, CRUDClient<T>(typeInfo, baseUrl, httpClient) {
//
//    private lateinit var channel: ByteWriteChannel
//
////    override suspend fun writeTo(channel: ByteWriteChannel) {
////        this.channel = channel
////        block(this)
////    }
//
//    public suspend fun write(operation: HttpCrud) {
//        channel.writeByteArrayWithLength(Json.Default.encodeToString(operation).encodeToByteArray())
//        channel.flush()
//    }
//
//    override suspend fun commit() {
//        channel.writeStringUtf8("""{"type":"Commit"}\n""")
//        channel.flushAndClose()
//    }
//
//    override suspend fun rollback() {
//        channel.writeStringUtf8("""{"type":"Rollback"}\n""")
//        channel.flushAndClose()
//    }
//}
