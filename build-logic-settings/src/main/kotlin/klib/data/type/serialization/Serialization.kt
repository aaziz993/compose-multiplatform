package klib.data.type.serialization

import klib.data.type.serialization.encoder.decodeFromAny
import klib.data.type.serialization.encoder.encodeToAny
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

public fun <T : Any> T.toAny(): Any =
    (this::class.serializer() as KSerializer<T>).encodeToAny(this)!!

public inline fun <reified T : Any> Any.fromAny(): T =
    T::class.serializer().decodeFromAny(this)

@Suppress("UNCHECKED_CAST")
public inline fun <reified T : Any> KSerializer<T>.copyFrom(value: T, block: Any.() -> Any = { this }): T =
    value.toAny().block().fromAny<T>()