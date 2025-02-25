package gradle.serialization

import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.reflections.Reflections

public fun <T : Any> KClass<out T>.getPolymorphicSerializer(type: String): KSerializer<T>? = Reflections().getSubTypesOf(java)
    .filter { it.kotlin.findAnnotation<Serializable>() != null }
    .find { clazz ->
        (clazz.getAnnotation(SerialName::class.java)?.value ?: clazz.simpleName) == type
    }?.kotlin?.serializer() as KSerializer<T>?
