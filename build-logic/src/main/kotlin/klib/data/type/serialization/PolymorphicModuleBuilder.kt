package klib.data.type.serialization

import klib.data.type.reflection.REFLECTIONS
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.serializerOrNull

@Suppress("UNCHECKED_CAST")
public fun <Base : Any> PolymorphicModuleBuilder<Base>.subclass(
    subclass: KClass<out Base>,
    serializer: KSerializer<out Base>
): Unit = subclass(subclass as KClass<Base>, serializer as KSerializer<Base>)

@Suppress("UNCHECKED_CAST")
public inline fun <Base : Any> PolymorphicModuleBuilder<Base>.subclass(
    subclass: KClass<out Base>,
    serializer: (typeSerializers: List<KSerializer<*>>) -> KSerializer<out Base>
): Unit = subclass(subclass, serializer(subclass.typeParameters.map { NothingSerializer() }))

public fun <T : Any> reflectionPolymorphicSubclasses(baseClass: KClass<T>): Map<KClass<out T>, KSerializer<out T>> =
    (if (baseClass.isSealed) baseClass.sealedSubclasses
    else REFLECTIONS.getSubTypesOf(baseClass.java).map(Class<out T>::kotlin))
        .filter { kClass -> kClass.hasAnnotation<Serializable>() }
        .mapNotNull { kClass ->
            kClass.serializerOrNull()?.let { serializer ->
                kClass to serializer
            }
        }.toMap()

public fun <Base : Any> PolymorphicModuleBuilder<Base>.subclasses(subclasses: Map<KClass<out Base>, KSerializer<out Base>>) {
    subclasses.forEach { (subclass, serializer) ->
        subclass(subclass, serializer)
    }
}

public fun <T : Any> PolymorphicModuleBuilder<T>.reflectionSubclasses(baseClass: KClass<T>): Unit =
    subclasses(reflectionPolymorphicSubclasses(baseClass))
