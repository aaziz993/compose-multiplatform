@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization

import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.throwSubtypeNotRegistered
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializerOrNull

@Suppress("UNCHECKED_CAST")
public fun <T : Any> SerializersModule.getPolymorphicOrValue(baseClass: KClass<in T>, value: T): KSerializer<T> =
    (getPolymorphic(baseClass, value)
        ?: value::class.serializerOrNull()
        ?: throwSubtypeNotRegistered(value::class, baseClass)) as KSerializer<T>
