package klib.data.type.serialization.serializers.transform

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames

public abstract class MapTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    public val keyAs: Any? = null,
    public val valueAs: Any? = null,
) : TransformingSerializer<T>(tSerializer) {

    @Suppress("UNCHECKED_CAST")
    override fun transformDeserialize(value: Any?): Any? =
        if (value is Map<*, *>) {
            if (value.keys.size == 1 && value.keys.single() !in tSerializer.descriptor.elementNames) {
                val key = value.keys.single().toString()
                val value = value.values.single()

                transformDeserializeKey(key) +
                    if (value is Map<*, *> && valueAs == null) value else transformDeserializeValue(key, value)
            }
            else value
        }
        else transformDeserializeKey(value)

    protected open fun transformDeserializeKey(key: Any?): Map<Any?, Any?> = mapOf(keyAs to key)

    protected open fun transformDeserializeValue(key: Any?, value: Any?): Map<Any?, Any?> = mapOf(valueAs to value)
}
