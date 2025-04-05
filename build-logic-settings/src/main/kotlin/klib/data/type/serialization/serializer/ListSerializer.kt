@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.internal.ArrayListClassDesc

internal class ArrayListSerializer<E, C : List<E>, B : ArrayList<E>>(
    eSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C
) : CollectionSerializer<E, C, B>(eSerializer, builder, toResult) {
    override val descriptor: SerialDescriptor = ArrayListClassDesc(eSerializer.descriptor)
}

@Suppress("FunctionName", "UNCHECKED_CAST")
public fun <E, C : List<E>, B : ArrayList<E>> ListSerializer(
    elementSerializer: KSerializer<E>,
    builder: () -> B,
    toResult: B.() -> C = { this as C }
): KSerializer<C> = ArrayListSerializer(elementSerializer, builder, toResult)
