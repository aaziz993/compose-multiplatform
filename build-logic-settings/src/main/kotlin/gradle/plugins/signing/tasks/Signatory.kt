package gradle.plugins.signing.tasks

import klib.data.type.serialization.serializer.JsonContentPolymorphicSerializer
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable(with = SignatorySerializer::class)
internal interface Signatory {

    context(Project)
    fun toSignatory(): org.gradle.plugins.signing.signatory.Signatory
}

private object SignatorySerializer : JsonContentPolymorphicSerializer<Signatory>(
    Signatory::class,
)
