package gradle.model.kmp.nat

import gradle.model.DefaultTestFilter
import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: DefaultTestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String = ""
) : KotlinNativeBinaryTestRun
