package klib.data.crud

import klib.data.query.AggregateExpression
import klib.data.query.BooleanOperand
import klib.data.query.LimitOffset
import klib.data.query.Order
import klib.data.query.Variable
import klib.data.transaction.Transaction

public interface CrudRepository<T : Any> {

    public fun <R> transactional(block: CrudRepository<T>.(Transaction) -> R): R

    public fun find(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Sequence<T>

    public fun find(
        properties: List<Variable>,
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Sequence<List<Any?>>

    public fun observe(
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Sequence<T> = emptySequence()

    public fun observe(
        properties: List<Variable>,
        predicate: BooleanOperand? = null,
        orderBy: List<Order> = emptyList(),
        limitOffset: LimitOffset? = null
    ): Sequence<List<Any?>> = emptySequence()

    public fun <T : Any> aggregate(
        aggregate: AggregateExpression<T>,
        predicate: BooleanOperand? = null,
    ): T?

    public fun insert(entities: List<T>): List<T>

    public fun insert(vararg entities: T): List<T> = insert(entities.toList())

    public fun update(properties: Map<String, Any?>, predicate: BooleanOperand? = null): Long

    public suspend fun update(entity: T): Boolean

    public fun upsert(entities: List<T>, predicate: BooleanOperand? = null): List<T>

    public fun upsert(vararg entities: T, predicate: BooleanOperand? = null): List<T> =
        upsert(entities.toList(), predicate)

    public fun delete(predicate: BooleanOperand? = null): Long
}
