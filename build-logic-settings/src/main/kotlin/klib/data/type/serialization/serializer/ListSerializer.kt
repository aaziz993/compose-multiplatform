@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.ArrayListClassDesc

internal class ArrayListSerializer<E, C : List<E>, B>(
    eSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C
) : CollectionSerializer<E, C, B>(eSerializer, builder, toResult) where B : C, B : MutableList<E> {
    override val descriptor: SerialDescriptor = ArrayListClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName")
public fun <E, C : List<E>, B> ListSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C = { this as C }
): KSerializer<C> where B : C, B : MutableList<E> = ArrayListSerializer(elementSerializer, builder, toResult)
