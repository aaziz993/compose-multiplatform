package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.ReflectionPolymorphicSerializer
import kotlin.reflect.KClass

public open class ReflectionMapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value"
) : MapTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
        classDiscriminator,
        valueDiscriminator,
    ),
    classDiscriminator,
    valueDiscriminator,
)
