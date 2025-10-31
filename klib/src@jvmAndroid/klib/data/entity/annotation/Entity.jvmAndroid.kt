package klib.data.entity.annotation

import klib.data.type.collections.sortedByReferences
import klib.data.type.reflection.annotatedTypes
import klib.data.type.reflection.toKClass
import klib.data.type.serialization.elementIndices
import klib.data.type.serialization.getAnnotations
import klib.data.type.serialization.getElementAnnotation
import kotlin.reflect.KClass
import kotlinx.serialization.serializer
import org.reflections.util.FilterBuilder

@Suppress("UNCHECKED_CAST")
public fun getEntities(vararg packages: String): List<KClass<*>> =
    Entity::class.annotatedTypes {
        forPackages(*packages)
            .filterInputsBy(
                FilterBuilder().apply {
                    packages.forEach(::includePackage)
                },
            )
    }.sortedByFkReferences()

@Suppress("UNCHECKED_CAST")
private fun List<KClass<*>>.sortedByFkReferences(): List<KClass<*>> =
    sortedByReferences { entities ->
        val descriptor = serializer().descriptor
        (descriptor.getAnnotations<FkReference>()
            .map { reference ->
                reference.targetEntity
            } + descriptor.elementIndices.mapNotNull { index ->
            descriptor.getElementAnnotation<FkReference>(index)?.targetEntity
        }).map(String::toKClass)
    }
