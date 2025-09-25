package klib.data.type.serialization

import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor

public fun ClassSerialDescriptorBuilder.element(
    descriptor: SerialDescriptor, index: Int,
    elementName: String? = null,
    elementDescriptor: SerialDescriptor? = null,
    elementAnnotations: List<Annotation>? = null,
    isElementOptional: Boolean? = null,
): Unit = element(
    elementName ?: descriptor.getElementName(index),
    elementDescriptor ?: descriptor.getElementDescriptor(index),
    elementAnnotations ?: descriptor.getElementAnnotations(index),
    isElementOptional ?: descriptor.isElementOptional(index),
)

public fun buildMapClassSerialDescriptor(
    descriptor: SerialDescriptor,
    keys: Set<String>,
    valueDescriptor: SerialDescriptor,
): SerialDescriptor = buildClassSerialDescriptor("MapClass", descriptor) {
    (0 until descriptor.elementsCount).forEach { index ->
        element(descriptor, index)
    }

    keys.forEach { key ->
        element(key, valueDescriptor, isOptional = valueDescriptor.isNullable)
    }
}
