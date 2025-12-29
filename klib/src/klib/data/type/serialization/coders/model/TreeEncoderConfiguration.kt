package klib.data.type.serialization.coders.model

import klib.data.type.serialization.classDiscriminator
import kotlinx.serialization.descriptors.SerialDescriptor

public open class TreeEncoderConfiguration(
    public var encodeDefaults: Boolean = true,
    override var serializableValueMark: (SerialDescriptor, index: Int, value: Any?) -> Boolean = { _, _, _ -> true },
    override var classDiscriminator: (SerialDescriptor) -> String = { descriptor -> descriptor.classDiscriminator },
    override var filterElement: (SerialDescriptor, Int, Any?) -> Boolean = { _, _, _ -> true },
    override var transformElement: (SerialDescriptor, Int, Any?) -> Any? = { _, index, element -> element },
    override var transformEntry: (SerialDescriptor, Int, Map.Entry<Any?, Any?>) -> Pair<Any?, Any?>? =
        { _, _, (key, value) ->
            key to value
        },
    public var transformProperty: (SerialDescriptor, index: Int, value: Any?) -> Pair<String, Any?>? =
        { descriptor, index, value ->
            descriptor.getElementName(index) to value
        },
) : TreeCoderConfiguration

