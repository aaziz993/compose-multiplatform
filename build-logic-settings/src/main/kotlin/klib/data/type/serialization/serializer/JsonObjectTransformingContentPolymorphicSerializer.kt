package klib.data.type.serialization.serializer

import kotlin.reflect.KClass

public open class JsonObjectTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    keyAs: String = classDiscriminator,
    valueAs: String? = null,
) : JsonObjectTransformingSerializer<T>(
    JsonContentPolymorphicSerializer(
        baseClass,
        classDiscriminator,
    ),
    keyAs,
    valueAs,
    baseClass.polymorphicSerialNames.toSet(),
)
