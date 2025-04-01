package gradle.plugins.signing.tasks

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.plugins.signing.SignatureSpec

internal interface SignatureSpec<T : SignatureSpec> {

    val signatory: Signatory?

    val signatureType: @Serializable(with = SignatureTypeContentPolymorphicSerializer::class) Any?

    val required: Boolean?

    context(Project)
    fun applyTo(receiver: T) {
        receiver::setSignatory trySet signatory?.toSignatory()

        when (val signatureType = signatureType) {
            is SignatureType -> receiver.signatureType = signatureType.value
            is WorkaroundSignatureType -> receiver.signatureType = signatureType.toSignatureType()
            else -> Unit
        }

        receiver::setRequired trySet required
    }
}
