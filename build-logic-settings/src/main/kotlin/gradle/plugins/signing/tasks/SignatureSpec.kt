package gradle.plugins.signing.tasks

import gradle.api.trySet
import org.gradle.api.Project
import org.gradle.plugins.signing.SignatureSpec

internal interface SignatureSpec<T : SignatureSpec> {

    val signatory: Signatory?

    val signatureType: SignatureType?

    val required: Boolean?

    context(Project)
    fun applyTo(receiver: T) {
        receiver::setSignatory trySet signatory?.toSignatory()
        receiver::setSignatureType trySet signatureType?.value
        receiver::setRequired trySet required
    }
}
