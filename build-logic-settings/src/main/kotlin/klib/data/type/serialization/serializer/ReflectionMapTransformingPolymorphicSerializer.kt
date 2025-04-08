package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public open class ReflectionMapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    keyAs: String = "type",
    valueAs: String? = null,
) : MapTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
    ),
    keyAs,
    valueAs,
)
