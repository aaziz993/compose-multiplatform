package gradle.model

import gradle.serialization.serializer.AnySerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class Expand(
    val properties:Map<String,@Serializable(with= AnySerializer::class) Any>,
    val expandDetails: ExpandDetails
)
