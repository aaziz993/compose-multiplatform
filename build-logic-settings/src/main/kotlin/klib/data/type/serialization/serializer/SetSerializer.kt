@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.LinkedHashSetClassDesc

internal class LinkedHashSetSerializer<E, C : Set<E>, B : LinkedHashSet<E>>(
    eSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C
) : CollectionSerializer<E, C, B>(
    eSerializer,
    builder,
    toResult
) {
    override val descriptor: SerialDescriptor = LinkedHashSetClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
public fun <E, C : Set<E>, B : LinkedHashSet<E>> SetSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C = { this as C }
): KSerializer<C> = LinkedHashSetSerializer(elementSerializer, builder, toResult)
