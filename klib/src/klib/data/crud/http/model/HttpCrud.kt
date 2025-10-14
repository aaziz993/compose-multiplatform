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

    @Serializable
    public data class Insert<out T : Any>(val values: List<T>) : HttpCrud()

    @Serializable
    public data class InsertAndReturn<out T : Any>(val values: List<T>) : HttpCrud()

    @Serializable
    public data class Update<out T : Any>(val values: List<T>) : HttpCrud()

    @Serializable
    public data class UpdateProjections(
        val values: List<SerializableNullableAnyMap>,
        val predicate: BooleanVariable?
    ) : HttpCrud()

    @Serializable
    public data class Upsert<out T : Any>(val values: List<T>) : HttpCrud()

    @Serializable
    public data class Find(
        val sort: List<Order>? = null,
        val predicate: BooleanVariable? = null,
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class FindProjections(
        val projections: List<Variable>? = null,
        val sort: List<Order>? = null,
        val predicate: BooleanVariable? = null,
        val limitOffset: LimitOffset? = null,
    ) : HttpCrud()

    @Serializable
    public data class Delete(val predicate: BooleanVariable?) : HttpCrud()

    @Serializable
    public data class Aggregate(val aggregate: AggregateExpression<*>, val predicate: BooleanVariable?) : HttpCrud()
}
