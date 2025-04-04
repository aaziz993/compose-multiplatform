@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer

internal abstract class CollectionSerializer<T : MutableCollection<E>, E>(
    eSerializer: KSerializer<E>,
    private val _builder: () -> T,
) : kotlinx.serialization.internal.CollectionSerializer<E, T, T>(eSerializer) {

    override fun builder(): T = _builder()
    override fun T.builderSize(): Int = size
    override fun T.toResult(): T = this
    override fun T.toBuilder(): T = _builder()
    override fun T.checkCapacity(size: Int) {}
    override fun T.insert(index: Int, element: E) {
        add(element)
    }
}