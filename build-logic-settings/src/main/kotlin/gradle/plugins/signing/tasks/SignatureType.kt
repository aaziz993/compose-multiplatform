package gradle.plugins.signing.tasks

import com.vanniktech.maven.publish.tasks.WorkaroundSignatureType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.plugins.signing.type.BinarySignatureType
import org.gradle.plugins.signing.type.pgp.ArmoredSignatureType

@Serializable
internal sealed class SignatureType {

    context(Project)
    abstract fun toSignatureType(): org.gradle.plugins.signing.type.SignatureType

    @Serializable
    @SerialName("binary")
    object Binary : SignatureType() {

        context(Project)
        override fun toSignatureType(): org.gradle.plugins.signing.type.SignatureType =
            BinarySignatureType()
    }

    @Serializable
    @SerialName("armored")
    object Armored : SignatureType() {

        context(Project)
        override fun toSignatureType(): org.gradle.plugins.signing.type.SignatureType =
            ArmoredSignatureType()
    }

    @Serializable
    @SerialName("workaround")
    data class Workaround(val actual: SignatureType, val directory: String) : SignatureType() {

        context(Project)
        override fun toSignatureType(): org.gradle.plugins.signing.type.SignatureType =
            WorkaroundSignatureType(actual.toSignatureType(), project.layout.buildDirectory.dir(directory))
    }
}
