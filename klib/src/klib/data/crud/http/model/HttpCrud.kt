package klib.data.crud.http.model

import klib.data.query.AggregateExpression
import klib.data.query.LimitOffset
import klib.data.query.Order
import klib.data.query.Variable
import klib.data.type.serialization.serializers.collections.SerializableNullableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public sealed class HttpCrud {

    @Serializable
    public data class Find(
        val predicate: Variable? = null,
        val orderBy: List<Order> = emptyList(),
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class FindProperties(
        val properties: List<Variable>,
        val predicate: Variable? = null,
        val orderBy: List<Order> = emptyList(),
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class Observe(
        val predicate: Variable? = null,
        val orderBy: List<Order> = emptyList(),
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class ObserveProperties(
        val properties: List<Variable>,
        val predicate: Variable? = null,
        val orderBy: List<Order> = emptyList(),
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class Delete(val predicate: Variable? = null) : HttpCrud()

    @Serializable
    public data class Aggregate(
        val aggregate: AggregateExpression,
        val predicate: Variable? = null,
    ) : HttpCrud()

    @Serializable
    public data class Insert<out T : Any>(val entities: List<T>) : HttpCrud()

    @Serializable
    public data class Update(
        val properties: SerializableNullableAnyMap,
        val predicate: Variable? = null,
    ) : HttpCrud()

    @Serializable
    public data class UpdateEntity<out T : Any>(val entity: T) : HttpCrud()

    @Serializable
    public data class Upsert<out T : Any>(
        val entities: List<T>,
        val predicate: Variable? = null,
    ) : HttpCrud()
}
