@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.room

import androidx.room.gradle.RoomExtension
import klib.data.type.serialization.serializer.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = SchemaDirectoryMapTransformingSerializer::class)
internal data class SchemaDirectory(
    val matchName: String = RoomExtension.ALL_MATCH.actual,
    val path: String
)

private object SchemaDirectoryMapTransformingSerializer : MapTransformingSerializer<SchemaDirectory>(
    SchemaDirectory.generatedSerializer(),
    "path",
    "matchName",
)
