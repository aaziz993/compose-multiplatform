package klib.data.type.serialization.coders.model

import kotlinx.serialization.descriptors.SerialDescriptor

public interface TreeCoderConfiguration {

    public var serializableValueMark: (SerialDescriptor, index: Int) -> Boolean
    public var classDiscriminator: (SerialDescriptor) -> String
    public var filterElement: (SerialDescriptor, index: Int, element: Any?) -> Boolean
    public var transformElement: (SerialDescriptor, index: Int, element: Any?) -> Any?
    public var transformEntry: (SerialDescriptor, index: Int, Map.Entry<Any?, Any?>) -> Pair<Any?, Any?>?
}
