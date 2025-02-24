package plugin.project.java.model

import kotlinx.serialization.Serializable
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.jvm.toolchain.internal.DefaultJvmVendorSpec

@Serializable
internal data class JvmVendorSpec(
    val matches: String,
) {

    fun toVendor() = DefaultJvmVendorSpec.matching(matches)
}
