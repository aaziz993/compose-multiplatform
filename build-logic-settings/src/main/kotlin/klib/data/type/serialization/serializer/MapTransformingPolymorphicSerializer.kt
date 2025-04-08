package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public open class MapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    subclasses: List<KClass<out T>> = emptyList(),
    keyAs: String = "type",
    valueAs: String? = null,
) : MapTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        subclasses,
    ),
    keyAs,
    valueAs,
)
