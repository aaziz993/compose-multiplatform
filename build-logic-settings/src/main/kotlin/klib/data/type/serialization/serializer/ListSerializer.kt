package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer

@Suppress("UNCHECKED_CAST")
public open class ListSerializer<T : List<E>, E>(elementSerializer: KSerializer<E>) :
    KSerializer<T> by ListSerializer(elementSerializer) as KSerializer<T>
