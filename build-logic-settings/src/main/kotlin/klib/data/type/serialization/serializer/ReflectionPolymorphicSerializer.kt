package klib.data.type.serialization.serializer

import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlinx.serialization.Serializable
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

public open class ReflectionPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
) : PolymorphicSerializer<T>(
    baseClass,
    if (baseClass.isSealed) emptyList() else baseClass.getSerializableSubclasses(),
) {

    private companion object {

        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(""))
                .setScanners(Scanners.SubTypes),
        )

        fun <T : Any> KClass<T>.getSerializableSubclasses(): List<KClass<out T>> =
            reflections.getSubTypesOf(java)
                .map(Class<out T>::kotlin)
                .filter { klass -> klass.hasAnnotation<Serializable>() }
    }
}
