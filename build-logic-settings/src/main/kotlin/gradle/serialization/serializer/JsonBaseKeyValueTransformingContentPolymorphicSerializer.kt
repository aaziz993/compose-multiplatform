package gradle.serialization.serializer

import kotlin.reflect.KClass

public abstract class JsonBaseKeyValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
) : JsonBaseKeyValueTransformingSerializer<T>(
    JsonContentPolymorphicSerializer<T>(
        baseClass,
        classDiscriminator,
    ),
)
