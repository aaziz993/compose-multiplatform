package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public abstract class JsonBaseObjectTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
) : JsonBaseObjectTransformingSerializer<T>(
    JsonContentPolymorphicSerializer(
        baseClass,
        classDiscriminator,
    ),
    baseClass.polymorphicSerialNames.toSet(),
)
