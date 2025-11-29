package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.PolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

public open class MapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    baseSerializer: KSerializer<T>? = null,
    subclasses: Map<KClass<out T>, KSerializer<out T>>,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value",
) : MapTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        baseSerializer,
        subclasses,
        classDiscriminator,
        valueDiscriminator,
    ),
    classDiscriminator,
    valueDiscriminator,
)
