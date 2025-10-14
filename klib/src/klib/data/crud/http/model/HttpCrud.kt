package klib.data.crud.http.model

import klib.data.AggregateExpression
import klib.data.BooleanVariable
import klib.data.Variable
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.type.serialization.serializers.collections.SerializableNullableAnyMap
import kotlinx.serialization.Serializable

@Serializable
public sealed class HttpCrud {

    public abstract val transactionId: String?

    @Serializable
    public data class Insert<out T : Any>(override val transactionId: String? = null, val values: List<T>) : HttpCrud()

    @Serializable
    public data class InsertAndReturn<out T : Any>(override val transactionId: String? = null, val values: List<T>) : HttpCrud()

    @Serializable
    public data class Update<out T : Any>(override val transactionId: String? = null, val values: List<T>) : HttpCrud()

    @Serializable
    public data class UpdateProjections(
        override val transactionId: String? = null,
        val values: List<SerializableNullableAnyMap>,
        val predicate: BooleanVariable? = null,
    ) : HttpCrud()

    @Serializable
    public data class Upsert<out T : Any>(override val transactionId: String? = null, val values: List<T>) : HttpCrud()

    @Serializable
    public data class Find(
        override val transactionId: String? = null,
        val sort: List<Order>? = null,
        val predicate: BooleanVariable? = null,
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class FindProjections(
        override val transactionId: String? = null,
        val projections: List<Variable>? = null,
        val sort: List<Order>? = null,
        val predicate: BooleanVariable? = null,
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class Delete(
        override val transactionId: String? = null,
        val predicate: BooleanVariable? = null
    ) : HttpCrud()

    @Serializable
    public data class Aggregate(
        override val transactionId: String? = null,
        val aggregate: AggregateExpression<*>,
        val predicate: BooleanVariable? = null,
    ) : HttpCrud()
}
