package klib.data.type.collection

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

public interface DelegatedMap : Map<String, Any?> {

    public val delegate: SerializableOptionalAnyMap

    override val entries: Set<Map.Entry<String, Any?>>
        get() = delegate.entries

    override val keys: Set<String>
        get() = delegate.keys

    override val size: Int
        get() = delegate.size

    override val values: Collection<Any?>
        get() = delegate.values

    override fun containsKey(key: String): Boolean = delegate.containsKey(key)
    override fun containsValue(value: Any?): Boolean = delegate.containsValue(value)
    override fun get(key: String): Any? = delegate[key]
    override fun isEmpty(): Boolean = delegate.isEmpty()
}

public abstract class DelegatedMapTransformingSerializer<T : DelegatedMap>(private val tSerializer: KSerializer<T>) :
    JsonTransformingSerializer<T>(tSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement = JsonObject(
        buildMap {
            putAll(element.jsonObject.slice(tSerializer.descriptor.elementNames))
            put("delegate", JsonObject(element.jsonObject - tSerializer.descriptor.elementNames))
        },
    )

    override fun transformSerialize(element: JsonElement): JsonElement = JsonObject(
            buildMap {
                putAll(element.jsonObject - "delegate")
                putAll(element.jsonObject["delegate"]!!.jsonObject)
            },
    )
}
