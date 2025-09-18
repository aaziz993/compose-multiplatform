package klib.data.type.serialization

import klib.data.type.reflection.REFLECTIONS
import klib.data.type.serialization.hasAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.serializerOrNull

@Suppress("UNCHECKED_CAST")
public inline fun <Base : Any, reified T : Base> PolymorphicModuleBuilder<Base>.subclass(
    subclass: KClass<T>,
    serializer: (typeSerializers: List<KSerializer<*>>) -> KSerializer<*>
): Unit = subclass(subclass, serializer(subclass.typeParameters.map { NothingSerializer() }) as KSerializer<T>)

public fun <T : Any> reflectionPolymorphicSubclasses(baseClass: KClass<T>): Map<KClass<T>, KSerializer<T>> =
    (if (baseClass.isSealed) baseClass.sealedSubclasses
    else REFLECTIONS.getSubTypesOf(baseClass.java).map(Class<out T>::kotlin))
        .filter { kClass -> kClass.hasAnnotation<Serializable>() }
        .mapNotNull { kClass ->
            kClass.serializerOrNull()?.let { serializer ->
                kClass as KClass<T> to serializer as KSerializer<T>
            }
        }.toMap()

public fun <T : Any> PolymorphicModuleBuilder<T>.subclasses(value: Map<KClass<T>, KSerializer<T>>): Unit =
    value.forEach(::subclass)

public fun <T : Any> PolymorphicModuleBuilder<T>.reflectionSubclasses(baseClass: KClass<T>): Unit =
    subclasses(reflectionPolymorphicSubclasses(baseClass))
