package plugin.project.gradle.kover.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KoverSettings(
    val enabled: Boolean = true,
    override val useJacoco: Boolean? = null,
    override val jacocoVersion: String? = null,
    override val currentProject: KoverCurrentProjectVariantsConfig? = null,
    override val reports: KoverReportsConfig? = null,
) : KoverExtension
