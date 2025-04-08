package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames

public abstract class BaseMapTransformingSerializer<T : Any>(
    private val tSerializer: KSerializer<T>,
) : TransformingSerializer<T>(tSerializer) {

    public abstract fun transformDeserialize(key: String, value: Any?): Map<String, Any?>

    final override fun transformDeserialize(value: Any?): Any? =
        if (value is Map<*, *>) {
            if (value.keys.size == 1 && value.keys.single() !in tSerializer.descriptor.elementNames) {
                val key = value.keys.single().toString()
                val value = value.values.single()

                transformDeserialize(key, value) + (value as? Map<*, *> ?: transformDeserialize(key, value))
            } else value
        } else transformDeserialize(value.toString(), null)
}
