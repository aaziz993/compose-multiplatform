package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.ReflectionPolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

public open class ReflectionMapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    baseSerializer: KSerializer<T>? = null,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value"
) : MapTransformingSerializer<T>(
    ReflectionPolymorphicSerializer(
        baseClass,
        baseSerializer,
        classDiscriminator,
        valueDiscriminator,
    ),
    classDiscriminator,
    valueDiscriminator,
)
