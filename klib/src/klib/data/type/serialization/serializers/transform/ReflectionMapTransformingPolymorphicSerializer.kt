package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.ReflectionPolymorphicSerializer
import kotlin.reflect.KClass

public open class ReflectionMapTransformingPolymorphicSerializer<T : Any>(
    public val baseClass: KClass<T>,
    keyAs: String = "type",
    valueAs: String? = null,
) : MapTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
    ),
    keyAs,
    valueAs,
)
