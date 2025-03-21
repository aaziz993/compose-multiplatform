package gradle.plugins.signing.tasks

import org.gradle.plugins.signing.type.BinarySignatureType
import org.gradle.plugins.signing.type.SignatureType
import org.gradle.plugins.signing.type.pgp.ArmoredSignatureType

internal enum class SignatureType(val value: SignatureType) {
    BINARY(BinarySignatureType()),
    ARMORED(ArmoredSignatureType())
}
