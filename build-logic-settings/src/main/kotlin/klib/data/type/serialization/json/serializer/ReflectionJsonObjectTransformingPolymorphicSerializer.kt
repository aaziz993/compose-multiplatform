package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.serializer.ReflectionPolymorphicSerializer
import kotlin.reflect.KClass

public open class ReflectionJsonObjectTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    keyAs: String = "type",
    valueAs: String? = null,
) : JsonObjectTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
    ),
    keyAs,
    valueAs,
)
