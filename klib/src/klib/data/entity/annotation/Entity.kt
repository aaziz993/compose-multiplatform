package klib.data.entity.annotation

import klib.data.type.collections.bimap.BiMap
import klib.data.type.collections.bimap.toBiMap
import klib.data.type.serialization.elementIndices
import klib.data.type.serialization.getAnnotation
import klib.data.type.serialization.getElementAnnotation
import klib.data.type.serialization.hasElementAnnotation
import kotlin.reflect.KClass
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.serializer

@SerialInfo
@Target(AnnotationTarget.CLASS)
public annotation class Entity(val name: String = "")

public fun KClass<*>.getEntityProperties(): BiMap<String, String> =
    serializer().descriptor.let { descriptor ->
        descriptor.elementIndices.filter { index ->
            !descriptor.hasElementAnnotation<Transient>(index)
        }.associate { index ->
            descriptor.getElementName(index).let { elementName ->
                elementName to (descriptor.getElementAnnotation<Column>(index)?.name
                    ?: elementName)
            }
        }.toBiMap()
    }

public fun KClass<*>.getEntityGeneratedProperties(): List<String> =
    annotatedProperties<AutoIncrement>() + annotatedProperties<DatabaseGenerated>()

public fun KClass<*>.getEntityIdProperties(): List<String> =
    serializer().descriptor.getAnnotation<PrimaryKey>()?.columns?.toList()
        ?: listOfNotNull(annotatedProperties<PrimaryKey>().firstOrNull())

public fun KClass<*>.getEntityCreatedAtProperty(): String? =
    annotatedProperties<CreatedAt>().firstOrNull()

public fun KClass<*>.getEntityUpdatedAtProperty(): String? =
    annotatedProperties<UpdatedAt>().firstOrNull()

private inline fun <reified T : Annotation> KClass<*>.annotatedProperties(): List<String> =
    serializer().descriptor.let { descriptor ->
        descriptor.elementIndices.filter { index -> descriptor.hasElementAnnotation<T>(index) }
            .map { index ->
                descriptor.getElementAnnotation<Column>(index)?.name
                    ?: descriptor.getElementName(index)
            }
    }
