@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.room

import androidx.room.gradle.RoomExtension
import gradle.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = SchemaDirectoryObjectTransformingSerializer::class)
internal data class SchemaDirectory(
    val matchName: String = RoomExtension.ALL_MATCH.actual,
    val path: String
)

private object SchemaDirectoryObjectTransformingSerializer : JsonObjectTransformingSerializer<SchemaDirectory>(
    SchemaDirectory.generatedSerializer(),
    "path",
    "matchName",
)
