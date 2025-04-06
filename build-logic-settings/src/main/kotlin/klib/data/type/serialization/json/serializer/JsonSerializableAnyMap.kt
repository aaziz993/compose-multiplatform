package klib.data.type.serialization.json.serializer

import kotlinx.serialization.Serializable

public typealias SerializableAnyMap = Map<String, @Serializable(with = JsonAnySerializer::class) Any>
