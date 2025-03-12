package gradle.collection

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

internal typealias SerializableAnyList = List<@Serializable(with = AnySerializer::class) Any>
