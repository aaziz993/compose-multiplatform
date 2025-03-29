@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kotlin.room

import androidx.room.gradle.RoomExtension
import gradle.serialization.serializer.KeyTransformingSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = SchemaDirectoryKeyTransformingSerializer::class)
internal data class SchemaDirectory(
    val matchName: String = RoomExtension.ALL_MATCH.actual,
    val path: String
)

private object SchemaDirectoryKeyTransformingSerializer : KeyTransformingSerializer<SchemaDirectory>(
    SchemaDirectory.generatedSerializer(),
    "path",
    "matchName",
)
