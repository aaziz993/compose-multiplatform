package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.serializer.PolymorphicSerializer
import kotlin.reflect.KClass

public abstract class JsonBaseObjectTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    subclasses: List<KClass<out T>> = emptyList()
) : JsonBaseObjectTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        subclasses,
    ),
)
