@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.LinkedHashSetClassDesc

internal class LinkedHashSetSerializer<T : LinkedHashSet<E>, E>(
    eSerializer: KSerializer<E>,
    _builder: () -> T,
) : CollectionSerializer<T, E>(
    eSerializer,
    _builder
) {
    override val descriptor: SerialDescriptor = LinkedHashSetClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName")
public fun <T : LinkedHashSet<E>, E> SetSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> T
): KSerializer<T> = LinkedHashSetSerializer<T, E>(elementSerializer, builder)
