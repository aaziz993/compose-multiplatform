package klib.data.crud

import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.transaction.CoroutineTransaction
import klib.data.AggregateExpression
import klib.data.BooleanVariable
import klib.data.Variable
import kotlinx.coroutines.flow.Flow

public interface CoroutineCRUDRepository<T : Any> {

    public suspend fun <R> transactional(block: suspend CoroutineCRUDRepository<T>.(CoroutineTransaction) -> R): R

    public suspend fun insertAndReturn(entities: List<T>): List<T>

    public suspend fun insertAndReturn(vararg entities: T): List<T> = insertAndReturn(entities.toList())

    public suspend fun insert(entities: List<T>)

    public suspend fun insert(vararg entities: T): Unit = insert(entities.toList())

    public suspend fun update(entities: List<T>): List<Boolean>

    public suspend fun update(vararg entities: T): List<Boolean> = update(entities.toList())

    public suspend fun update(
        properties: List<Map<String, Any?>>,
        predicate: BooleanVariable? = null,
    ): Long

    public suspend fun upsert(entities: List<T>): List<T>

    public suspend fun upsert(vararg entities: T): List<T> = upsert(entities.toList())

    public fun find(
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
        limitOffset: LimitOffset? = null
    ): Flow<T>

    public fun find(
        projections: List<Variable>,
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
        limitOffset: LimitOffset? = null
    ): Flow<List<Any?>>

    public suspend fun delete(predicate: BooleanVariable? = null): Long

    public suspend fun <T> aggregate(
        aggregate: AggregateExpression<T>,
        predicate: BooleanVariable? = null,
    ): T
}
