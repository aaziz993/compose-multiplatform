package klib.data.type.serialization.json.serializer

import kotlinx.serialization.Serializable

public typealias SerializableOptionalAnyMap = Map<String, @Serializable(with = JsonOptionalAnySerializer::class) Any?>
