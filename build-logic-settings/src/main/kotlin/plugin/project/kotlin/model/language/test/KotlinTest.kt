package plugin.project.kotlin.model.language.test

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinTest(
    override val ignoreFailures: Boolean? = null,
    val targetName: String? = null,
    val filter: DefaultTestFilter? = null,
    val ignoreRunFailures: Boolean? = null,
) : AbstractTestTask
