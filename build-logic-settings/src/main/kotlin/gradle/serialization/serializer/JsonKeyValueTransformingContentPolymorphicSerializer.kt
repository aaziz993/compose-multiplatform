package gradle.serialization.serializer

import kotlin.reflect.KClass

public abstract class JsonBaseKeyValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
) : BaseKeyValueTransformingSerializer<T>(
    JsonContentPolymorphicSerializer<T>(
        baseClass,
        classDiscriminator,
    ),
)

public abstract class JsonKeyValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    keyAs: String = classDiscriminator,
    valueAs: String? = null,
) : KeyValueTransformingSerializer<T>(
    JsonContentPolymorphicSerializer<T>(
        baseClass,
        classDiscriminator,
    ),
    keyAs,
    valueAs,
)
