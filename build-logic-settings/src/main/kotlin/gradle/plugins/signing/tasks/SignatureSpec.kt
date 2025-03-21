package gradle.plugins.signing.tasks

import org.gradle.api.Project
import org.gradle.plugins.signing.SignatureSpec

internal interface SignatureSpec<T : SignatureSpec> {

    val signatory: Signatory?

    val signatureType: SignatureType?

    val required: Boolean?

    context(Project)
    fun applyTo(recipient: T) {
        signatory?.toSignatory()?.let(recipient::setSignatory)
        signatureType?.value?.let(recipient::setSignatureType)
        required?.let(recipient::setRequired)
    }
}
