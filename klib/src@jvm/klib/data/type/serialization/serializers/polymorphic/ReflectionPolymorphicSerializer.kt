package klib.data.type.serialization.serializers.polymorphic

import klib.data.type.serialization.reflectionPolymorphicSubclasses
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

public open class ReflectionPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    baseSerializer: KSerializer<T>? = null,
    classDiscriminator: String = "type",
    valueDiscriminator: String = "value"
) : PolymorphicSerializer<T>(
    baseClass,
    baseSerializer,
    reflectionPolymorphicSubclasses(baseClass),
    classDiscriminator,
    valueDiscriminator,
)
