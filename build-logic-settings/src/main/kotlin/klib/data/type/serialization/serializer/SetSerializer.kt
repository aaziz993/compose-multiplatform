@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")

package klib.data.type.serialization.serializer

import CollectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.LinkedHashSetClassDesc


internal class LinkedHashSetSerializer<E, C : Set<E>, B>(
    eSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C
) : CollectionSerializer<E, C, B>(
    eSerializer,
    builder,
    toResult
) where B : C, B : MutableSet<E> {
    override val descriptor: SerialDescriptor = LinkedHashSetClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName")
public fun <E, C : Set<E>, B> SetSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C = { this as C }
): KSerializer<C> where B : C, B : MutableSet<E> = LinkedHashSetSerializer(elementSerializer, builder, toResult)
