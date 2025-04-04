@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.ArrayListClassDesc


internal class ArrayListSerializer<T : ArrayList<E>, E>(
    eSerializer: KSerializer<E>,
    _builder: () -> T,
) : CollectionSerializer<T, E>(eSerializer, _builder) {
    override val descriptor: SerialDescriptor = ArrayListClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName")
public fun <T : java.util.ArrayList<E>, E> ListSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> T
): KSerializer<T> = ArrayListSerializer<T, E>(elementSerializer, builder)
