package gradle.api.tasks

import klib.data.type.serialization.serializer.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
internal data class Expand(
    val properties: SerializableAnyMap,
    val expandDetails: ExpandDetailsImpl
)
