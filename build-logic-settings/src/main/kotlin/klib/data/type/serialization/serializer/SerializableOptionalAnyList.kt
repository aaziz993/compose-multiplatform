package klib.data.type.serialization.serializer

import kotlinx.serialization.Serializable

public typealias SerializableOptionalAnyList = List<@Serializable(with = OptionalAnySerializer::class) Any?>
