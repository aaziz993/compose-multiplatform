package klib.data.type.collection

import klib.data.type.serialization.serializer.AnySerializer
import klib.data.type.serialization.serializer.OptionalAnySerializer
import kotlinx.serialization.Serializable

internal typealias SerializableAnyList = List<@Serializable(with = AnySerializer::class) Any>

internal typealias SerializableOptionalAnyList = List<@Serializable(with = OptionalAnySerializer::class) Any?>
