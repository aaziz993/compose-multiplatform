package klib.data.type.serialization.serializer

import kotlinx.serialization.Serializable

public typealias SerializableAnyMap = Map<String, @Serializable(with = AnySerializer::class) Any>
