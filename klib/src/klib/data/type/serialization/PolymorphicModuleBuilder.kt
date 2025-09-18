package klib.data.type.serialization

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@Suppress("UNCHECKED_CAST")
public inline fun <Base : Any, reified T : Base> PolymorphicModuleBuilder<Base>.subclass(
    subclass: KClass<T>,
    serializer: KSerializer<*>
): Unit = subclass(subclass, serializer as KSerializer<T>)

public fun <T : Any> PolymorphicModuleBuilder<T>.subclasses(value: Map<KClass<T>, KSerializer<T>>): Unit =
    value.forEach { (kClass, serializer) ->
        subclass(kClass, serializer)
    }
