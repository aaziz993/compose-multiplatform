package gradle.plugins.signing.tasks

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import klib.data.type.serialization.serializer.ContentPolymorphicSerializer

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
    ContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(value: Any?): DeserializationStrategy<Any> =
        if (value is String) SignatureType.serializer() else WorkaroundSignatureType.serializer()
}
