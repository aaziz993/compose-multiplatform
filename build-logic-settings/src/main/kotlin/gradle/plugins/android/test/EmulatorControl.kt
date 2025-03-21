package gradle.plugins.android.test

import com.android.build.api.dsl.EmulatorControl
import gradle.api.trySet
import gradle.collection.act
import kotlinx.serialization.Serializable

/**
 * Options for configuring Android Emulator Access
 *
 * When enabled, it will be possible to control the emulator through gRPC.
 */
@Serializable
internal data class EmulatorControl(
    /** True if emulator control should be enabled. */
    val enable: Boolean? = null,
    /** Set of endpoints to which access is granted, this is only required if
     * the method you wish to access is in the set of methods that require
     * authorization as defined in emulator_access.json used by the emulator
     * this test is running on.
     *
     * Details on which endpoints and what considerations are taken to make an endpoint
     * accessible is described in go/emu-grpc-integration.
     */
    val allowedEndpoints: Set<String>? = null,
    val setAllowedEndpoints: Set<String>? = null,
    /** The duration in seconds the test can access the gRPC endpoint.
     * The default value is 3600 (one hour).
     */
    val secondsValid: Int? = null
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: EmulatorControl) {
        recipient::enable trySet enable
        allowedEndpoints?.let(recipient.allowedEndpoints::addAll)
        setAllowedEndpoints?.act(recipient.allowedEndpoints::clear)?.let(recipient.allowedEndpoints::addAll)
        recipient::secondsValid trySet secondsValid
    }
}
