package gradle.api.tasks.copy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer

import klib.data.type.serialization.serializer.ContentPolymorphicSerializer



@Serializable
internal data class From(
    val sourcePath: String,
    val copySpec: CopySpecImpl,
)

internal object FromContentPolymorphicSerializer : ContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(value: Any?): DeserializationStrategy<Any> =
        if (value is String) String.serializer() else From.serializer()
}
