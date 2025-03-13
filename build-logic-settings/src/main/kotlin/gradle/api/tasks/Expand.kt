package gradle.api.tasks

import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
internal data class Expand(
    val properties: SerializableAnyMap,
    val expandDetails: ExpandDetails
)
