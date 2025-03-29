package gradle.serialization.serializer

import kotlin.reflect.KClass



public open class JsonKeyValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    keyAs: String = classDiscriminator,
    valueAs: String? = null,
) : JsonKeyValueTransformingSerializer<T>(
    JsonContentPolymorphicSerializer(
        baseClass,
        classDiscriminator,
    ),
    keyAs,
    valueAs,
)
