package klib.data.type.serialization.serializer

import kotlinx.serialization.Serializable

public typealias SerializableOptionalAnyMap = Map<String, @Serializable(with = OptionalAnySerializer::class) Any?>
