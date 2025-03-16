package gradle.collection

import gradle.serialization.serializer.AnySerializer
import gradle.serialization.serializer.OptionalAnySerializer
import kotlinx.serialization.Serializable

internal typealias SerializableAnyList = List<@Serializable(with = AnySerializer::class) Any>

internal typealias SerializableOptionalAnyList = List<@Serializable(with = OptionalAnySerializer::class) Any?>
