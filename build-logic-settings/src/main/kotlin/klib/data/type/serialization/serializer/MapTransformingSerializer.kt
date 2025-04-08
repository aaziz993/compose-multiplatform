package klib.data.type.serialization.serializer

import kotlinx.serialization.KSerializer

public open class MapTransformingSerializer<T : Any>(
    tSerializer: KSerializer<T>,
    private val keyAs: String,
    private val valueAs: String? = null,
) : BaseMapTransformingSerializer<T>(tSerializer) {

    override fun transformDeserialize(key: String, value: Any?): Map<String, Any?> =
        buildMap {
            put(keyAs, key)
            value?.let { value -> put(valueAs!!, value) }
        }
}
