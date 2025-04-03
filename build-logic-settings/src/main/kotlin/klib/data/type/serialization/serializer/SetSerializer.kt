package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.SetSerializer

@Suppress("UNCHECKED_CAST")
public open class SetSerializer<T : Set<E>, E>(elementSerializer: KSerializer<E>) :
    KSerializer<T> by SetSerializer(elementSerializer) as KSerializer<T>
