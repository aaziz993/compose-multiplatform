package plugin.project.gradle.spotless.model

internal interface BaseKotlinExtension {

    val diktat: DiktatConfig?
    val ktfmt: List<KtfmtConfig>?
    val ktlint: KtlintConfig?
}
