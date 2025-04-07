package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.serializer.PolymorphicSerializer
import kotlin.reflect.KClass

public open class JsonObjectTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    subclasses: List<KClass<out T>> = emptyList(),
    keyAs: String = "type",
    valueAs: String? = null,
) : JsonObjectTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        subclasses,
    ),
    keyAs,
    valueAs,
)
