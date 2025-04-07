package klib.data.type.serialization.json.serializer

import klib.data.type.serialization.serializer.PolymorphicSerializer
import klib.data.type.serialization.serializer.ReflectionPolymorphicSerializer
import kotlin.reflect.KClass

public abstract class ReflectionJsonBaseObjectTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
) : JsonBaseObjectTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
    ),
)
