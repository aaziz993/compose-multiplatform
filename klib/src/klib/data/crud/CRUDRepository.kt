package klib.data.crud

import klib.data.AggregateExpression
import klib.data.BooleanVariable
import klib.data.Variable
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.transaction.Transaction

public interface CRUDRepository<T : Any> {

    public fun <R> transactional(block: CRUDRepository<T>.(Transaction) -> R): R

    public fun insertAndReturn(entities: List<T>): List<T>

    public fun insertAndReturn(vararg entities: T): List<T> = insertAndReturn(entities.toList())

    public fun insert(entities: List<T>)

    public fun insert(vararg entities: T): Unit = insert(entities.toList())

    public fun update(entities: List<T>): List<Boolean>

    public fun update(vararg entities: T): List<Boolean> = update(entities.toList())

    public fun update(
        properties: List<Map<String, Any?>>,
        predicate: BooleanVariable? = null,
    ): Long

    public fun upsert(entities: List<T>): List<T>

    public fun upsert(vararg entities: T): List<T> = upsert(entities.toList())

    public fun find(
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
        limitOffset: LimitOffset? = null
    ): Iterable<T>

    public fun find(
        projections: List<Variable>,
        sort: List<Order>? = null,
        predicate: BooleanVariable? = null,
        limitOffset: LimitOffset? = null
    ): Iterable<List<Any?>>

    public fun delete(predicate: BooleanVariable? = null): Long

    public fun <T> aggregate(
        aggregate: AggregateExpression<T>,
        predicate: BooleanVariable? = null,
    ): T
}
