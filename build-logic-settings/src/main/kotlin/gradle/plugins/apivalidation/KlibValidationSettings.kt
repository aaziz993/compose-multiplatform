package gradle.plugins.apivalidation

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import kotlinx.validation.ExperimentalBCVApi
import kotlinx.validation.KlibValidationSettings
import kotlinx.validation.api.klib.KlibSignatureVersion

@Serializable
internal open class KlibValidationSettings(
    /**
     * Enables KLib ABI validation checks.
     */
    val enabled: Boolean? = null,

    /**
     * Specifies which version of signature KLib ABI dump should contain.
     * By default, or when explicitly set to null, the latest supported version will be used.
     *
     * This option covers some advanced scenarios and does not require any configuration by default.
     *
     * A linker uses signatures to look up symbols, thus signature changes brake binary compatibility and
     * should be tracked. Signature format itself is not stabilized yet and may change in the future. In that case,
     * a new version of a signature will be introduced. Change of a signature version will be reflected in a dump
     * causing a validation failure even if declarations itself remained unchanged.
     * However, if a klib supports multiple signature versions simultaneously, one my explicitly specify the version
     * that will be dumped to prevent changes in a dump file.
     */
    val signatureVersion: Int? = null,

    /**
     * Fail validation if some build targets are not supported by the host compiler.
     * By default, ABI dumped only for supported files will be validated. This option makes validation behavior
     * stricter and treats having unsupported targets as an error.
     */
    val strictValidation: Boolean? = null
) {

    @OptIn(ExperimentalBCVApi::class)
    fun applyTo(receiver: KlibValidationSettings) {
        receiver::enabled trySet enabled
        receiver::signatureVersion trySet signatureVersion?.let(KlibSignatureVersion::of)
        receiver::strictValidation trySet strictValidation
    }
}
