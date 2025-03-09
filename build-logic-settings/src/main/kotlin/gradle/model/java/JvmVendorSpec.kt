package gradle.model.java

import kotlinx.serialization.Serializable
import org.gradle.jvm.toolchain.internal.DefaultJvmVendorSpec

@Serializable
internal data class JvmVendorSpec(
    val matches: String,
) {

    fun toVendor() = DefaultJvmVendorSpec.matching(matches)
}
