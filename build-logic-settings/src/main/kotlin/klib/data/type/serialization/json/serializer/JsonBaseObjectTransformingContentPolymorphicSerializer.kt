package klib.data.type.serialization.json.serializer

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
