package gradle.api.tasks.copy

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import klib.data.type.serialization.serializer.ContentPolymorphicSerializer

@Serializable
internal data class Into(
    val destPath: String,
    val copySpec: CopySpecImpl,
)

internal object IntoContentPolymorphicSerializer : ContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(value: Any?): DeserializationStrategy<Any> =
        if (value is String) String.serializer() else Into.serializer()
}
