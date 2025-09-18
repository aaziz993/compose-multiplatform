package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.PolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

public open class MapTransformingPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    baseSerializer: KSerializer<T>? = null,
    subclasses: Map<KClass<T>, KSerializer<T>>,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value",
    keyAs: String = "type",
    valueAs: String? = null,
) : MapTransformingSerializer<T>(
    PolymorphicSerializer(
        baseClass,
        baseSerializer,
        subclasses,
        classDiscriminator,
        valueDiscriminator,
    ),
    keyAs,
    valueAs,
)
