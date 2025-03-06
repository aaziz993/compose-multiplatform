package plugin.project.kmp.model.nat

import kotlinx.serialization.Serializable
import plugin.project.kotlin.kmp.model.test.TestFilter

@Serializable
internal data class KotlinNativeBinaryTestRunImpl(
    override val filter: TestFilter? = null,
    override val executionSource: NativeBinaryTestRunSource? = null,
    override val name: String = ""
) : KotlinNativeBinaryTestRun
