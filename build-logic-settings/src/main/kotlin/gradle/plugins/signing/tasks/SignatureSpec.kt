package gradle.plugins.signing.tasks

import org.gradle.api.Project
import org.gradle.plugins.signing.SignatureSpec

internal interface SignatureSpec<T : SignatureSpec> {

    val signatory: Signatory?

    val signatureType: SignatureType?

    val required: Boolean?

    context(project: Project)
    fun applyTo(receiver: T) {
        signatory?.toSignatory()?.let(receiver::setSignatory)
        signatureType?.value?.let(receiver::setSignatureType)
        required?.let(receiver::setRequired)
    }
}
