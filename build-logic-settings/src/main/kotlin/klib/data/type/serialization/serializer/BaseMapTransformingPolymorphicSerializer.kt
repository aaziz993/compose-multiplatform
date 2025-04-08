package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public abstract class BaseMapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    subclasses: List<KClass<out T>> = emptyList()
) : BaseMapTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        subclasses,
    ),
)
