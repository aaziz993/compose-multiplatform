package klib.data.type.serialization.coders.model

import klib.data.type.serialization.classDiscriminator
import klib.data.type.serialization.typeParametersSerializers
import klib.data.type.tuples.to
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor

public open class TreeDecoderConfiguration(
    public var decodeEnumsCaseInsensitive: Boolean = false,
    public var ignoreUnknownKeys: (SerialDescriptor) -> Boolean = { true },
    public var decodeNotNullMark: (value: Any?) -> Boolean = { value -> value != null },
    override var serializableValueMark: (SerialDescriptor, index: Int) -> Boolean = { _, _ -> true },
    override var classDiscriminator: (SerialDescriptor) -> String = { descriptor -> descriptor.classDiscriminator },
    override var filterElement: (SerialDescriptor, Int, Any?) -> Boolean = { _, _, _ -> true },
    override var transformElement: (SerialDescriptor, Int, Any?) -> Any? = { _, index, element -> element },
    override var transformEntry: (SerialDescriptor, Int, Map.Entry<Any?, Any?>) -> Pair<Any?, Any?>? =
        { _, _, (key, value) ->
            key to value
        },
    public var transformProperty: (SerialDescriptor, index: Int, map: Map<Any?, Any?>) -> Pair<String, Any?>? =
        { descriptor, index, map ->
            descriptor.getElementName(index).takeIf(map::containsKey)?.let { name ->
                name to map::get
            }
        },
    public var typeParameterSerializer: (KSerializer<*>, index: Int) -> KSerializer<*> = { serializer, index ->
        serializer.typeParametersSerializers[0]
    },
    public var serializationContext: (SerialDescriptor, index: Int) -> Any? = { _, _ -> null },
    public var computeValue: (SerialDescriptor, index: Int, value: String) -> Any? = { _, _, _ -> null }
) : TreeCoderConfiguration
