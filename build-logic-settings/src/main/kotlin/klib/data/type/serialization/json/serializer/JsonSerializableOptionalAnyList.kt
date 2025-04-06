package klib.data.type.serialization.json.serializer

import kotlinx.serialization.Serializable

public typealias SerializableOptionalAnyList = List<@Serializable(with = JsonOptionalAnySerializer::class) Any?>
