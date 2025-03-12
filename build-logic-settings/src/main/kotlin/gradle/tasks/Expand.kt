package gradle.tasks

import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
internal data class Expand(
    val properties:Map<String,@Serializable(with= AnySerializer::class) Any>,
    val expandDetails: ExpandDetails
)
