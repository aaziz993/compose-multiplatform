package klib.data.type.serialization.serializers.transform

import klib.data.type.serialization.serializers.polymorphic.PolymorphicSerializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder

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
