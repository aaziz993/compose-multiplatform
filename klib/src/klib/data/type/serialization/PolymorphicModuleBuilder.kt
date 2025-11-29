package klib.data.type.serialization

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@Suppress("UNCHECKED_CAST")
public fun <Base : Any> PolymorphicModuleBuilder<Base>.subclass(
    subclass: KClass<out Base>,
    serializer: KSerializer<out Base>
): Unit = subclass(subclass as KClass<Base>, serializer as KSerializer<Base>)

public fun <Base : Any> PolymorphicModuleBuilder<Base>.subclasses(subclasses: Map<KClass<out Base>, KSerializer<out Base>>) {
    subclasses.forEach { (subclass, serializer) ->
        subclass(subclass, serializer)
    }
}
