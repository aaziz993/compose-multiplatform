package gradle.collection

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

internal typealias SerializableAnyMap = List<@Serializable(with = AnySerializer::class) Any>
