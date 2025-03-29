package gradle.serialization.serializer

import kotlin.reflect.KClass

public abstract class JsonBaseKeyTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
) : BaseKeyTransformingSerializer<T>(
    JsonContentPolymorphicSerializer<T>(
        baseClass,
        classDiscriminator,
    ),
)

public abstract class JsonKeyTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    keyAs: String = classDiscriminator,
    valueAs: String? = null,
) : KeyTransformingSerializer<T>(
    JsonContentPolymorphicSerializer<T>(
        baseClass,
        classDiscriminator,
    ),
    keyAs,
    valueAs,
)
