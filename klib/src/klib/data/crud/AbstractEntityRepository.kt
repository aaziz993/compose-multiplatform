package klib.data.crud

import klib.data.type.cast
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.coders.tree.serialize
import kotlinx.datetime.TimeZone
import kotlinx.serialization.KSerializer

public abstract class AbstractEntityRepository<T : Any> {

    public abstract val kSerializer: KSerializer<T>
    public abstract val createdAtProperty: String?
    public abstract val updatedAtProperty: String?

    public abstract val timeZone: TimeZone

    protected fun T.toProperties(): Map<String, Any?> = kSerializer.serialize(this).cast()

    protected fun Map<String, Any?>.toEntity(): T = kSerializer.deserialize(this)

    protected open fun createdAtNow(): Any {
        error("Not implemented")
    }

    protected fun Map<String, Any?>.withCreatedAtProperties(): Map<String, Any?> =
        if (createdAtProperty == null) this else this + (createdAtProperty!! to createdAtNow())

    protected fun T.withCreatedAtProperties(): Map<String, Any?> = toProperties().withCreatedAtProperties()

    public fun List<T>.withCreatedAtProperties(): List<Map<String, Any?>> = map { entity ->
        entity.withCreatedAtProperties()
    }

    protected fun T.withCreatedAtEntity(): T = withCreatedAtProperties().toEntity()

    protected open fun updatedAtNow(): Any {
        error("Not implemented")
    }

    protected fun Map<String, Any?>.withUpdatedAtProperties(): Map<String, Any?> =
        if (updatedAtProperty == null) this else this + (updatedAtProperty!! to updatedAtNow())

    protected fun T.withUpdatedAtProperties(): Map<String, Any?> = toProperties().withUpdatedAtProperties()

    public fun List<T>.withUpdatedAtProperties(): List<Map<String, Any?>> = map { entity ->
        entity.withUpdatedAtProperties()
    }

    protected fun T.withUpdatedAtEntity(): T = withUpdatedAtProperties().toEntity()
}
