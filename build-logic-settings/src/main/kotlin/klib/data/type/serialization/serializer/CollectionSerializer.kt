@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer

@OptIn(InternalSerializationApi::class)
internal abstract class CollectionSerializer<E, C : Collection<E>, B : MutableCollection<E>>(
    eSerializer: KSerializer<E>,
    private val _builder: () -> B,
    private val _toResult: B.() -> C
) : kotlinx.serialization.internal.CollectionSerializer<E, C, B>(eSerializer) {

    override fun builder(): B = _builder()
    override fun B.builderSize(): Int = size
    override fun B.toResult(): C = _toResult()
    override fun C.toBuilder(): B = _builder()
    override fun B.checkCapacity(size: Int) {}
    override fun B.insert(index: Int, element: E) {
        add(element)
    }
}
