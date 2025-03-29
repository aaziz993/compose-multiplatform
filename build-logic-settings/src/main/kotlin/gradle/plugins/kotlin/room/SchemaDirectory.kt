@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.room

import androidx.room.gradle.RoomExtension
import gradle.serialization.serializer.KeyValueTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = SchemaDirectoryKeyValueTransformingSerializer::class)
internal data class SchemaDirectory(
    val matchName: String = RoomExtension.ALL_MATCH.actual,
    val path: String
)

private object SchemaDirectoryKeyValueTransformingSerializer : KeyValueTransformingSerializer<SchemaDirectory>(
    SchemaDirectory.generatedSerializer(),
    "path",
    "matchName",
)
