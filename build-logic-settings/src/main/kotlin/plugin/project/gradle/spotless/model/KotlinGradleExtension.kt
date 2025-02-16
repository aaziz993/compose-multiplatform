package plugin.project.gradle.spotless.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KotlinGradleExtension(
    override val diktat: DiktatConfig? = null,
    override val ktfmt: List<KtfmtConfig>? = null,
    override val ktlint: KtlintConfig? = null
) : BaseKotlinExtension
