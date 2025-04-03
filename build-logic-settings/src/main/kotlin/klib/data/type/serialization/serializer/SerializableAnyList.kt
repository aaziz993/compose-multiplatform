package klib.data.type.serialization.serializer

import kotlinx.serialization.Serializable

public typealias SerializableAnyList = List<@Serializable(with = AnySerializer::class) Any>
