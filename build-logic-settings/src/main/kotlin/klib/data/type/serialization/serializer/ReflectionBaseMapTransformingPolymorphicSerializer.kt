package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public abstract class ReflectionBaseMapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
) : BaseMapTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
    ),
)
