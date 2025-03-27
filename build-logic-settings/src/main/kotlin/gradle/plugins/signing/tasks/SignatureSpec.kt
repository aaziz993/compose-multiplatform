package gradle.plugins.signing.tasks

import org.gradle.api.Project
import org.gradle.plugins.signing.SignatureSpec

internal interface SignatureSpec<T : SignatureSpec> {

    val signatory: Signatory?

    val signatureType: SignatureType?

    val required: Boolean?

    context(Project)
    fun applyTo(receiver: T) {
        signatory?.toSignatory()?.let(receiver::setSignatory)
        signatureType?.receiver::setSignatureType trySet value
        receiver::setRequired trySet required
    }
}
