package klib.data.crud

import klib.data.query.AggregateExpression
import klib.data.query.BooleanOperand
import klib.data.query.LimitOffset
import klib.data.query.Order
import klib.data.query.Variable
import klib.data.transaction.CoroutineTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public interface CoroutineCrudRepository<T : Any> {

    public suspend fun <R> transactional(block: suspend CoroutineCrudRepository<T>.(CoroutineTransaction) -> R): R

    public fun find(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Flow<T>

    public fun find(
        properties: List<Variable>,
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Flow<List<Any?>>

    public fun observe(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Flow<T> = emptyFlow()

    public fun observe(
        properties: List<Variable>,
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Flow<List<Any?>> = emptyFlow()

    public suspend fun <T : Any> aggregate(
        aggregate: AggregateExpression<T>,
        predicate: BooleanOperand? = null,
    ): T?

    public suspend fun insert(entities: List<T>): List<T>

    public suspend fun insert(vararg entities: T): List<T> = insert(entities.toList())

    public suspend fun update(
        properties: Map<String, Any?>,
        predicate: BooleanOperand? = null
    ): Long

    public suspend fun update(entity: T): Boolean

    public suspend fun upsert(entities: List<T>, predicate: BooleanOperand? = null): List<T>

    public suspend fun upsert(vararg entities: T, predicate: BooleanOperand? = null): List<T> =
        upsert(entities.toList(), predicate)

    public suspend fun delete(predicate: BooleanOperand? = null): Long
}
