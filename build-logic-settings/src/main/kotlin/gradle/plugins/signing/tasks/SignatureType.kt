package gradle.plugins.signing.tasks

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.plugins.signing.type.BinarySignatureType
import org.gradle.plugins.signing.type.pgp.ArmoredSignatureType

@Serializable
internal enum class SignatureType(val value: org.gradle.plugins.signing.type.SignatureType) {

    BINARY(BinarySignatureType()),
    ARMORED(ArmoredSignatureType()),
}

@Serializable
internal data class WorkaroundSignatureType(val actual: SignatureType, val directory: String) {

    context(Project)
    fun toSignatureType(): org.gradle.plugins.signing.type.SignatureType =
        com.vanniktech.maven.publish.tasks.WorkaroundSignatureType(
            actual.value, project.layout.buildDirectory.dir(directory),
        )
}

// Layout
internal object SignatureTypeContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) SignatureType.serializer()
        else WorkaroundSignatureType.serializer()
}
