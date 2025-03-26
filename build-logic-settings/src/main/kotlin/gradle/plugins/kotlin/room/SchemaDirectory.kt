@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.room

import androidx.room.gradle.RoomExtension
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class SchemaDirectory(
    val matchName: String = RoomExtension.ALL_MATCH.actual,
    val path: String
)

internal object SchemaDirectoryKeyTransformingSerializer : KeyTransformingSerializer<SchemaDirectory>(
    SchemaDirectory.serializer(),
    "path",
    "matchName",
)
