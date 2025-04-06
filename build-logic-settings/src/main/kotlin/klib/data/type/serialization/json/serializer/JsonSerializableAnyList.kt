package klib.data.type.serialization.json.serializer

import kotlinx.serialization.Serializable

public typealias SerializableAnyList = List<@Serializable(with = JsonAnySerializer::class) Any>
