package plugin.project.gradle.spotless.model

internal interface BaseKotlinExtension {

    val diktat: DiktatConfig?
    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    val ktfmt: List<KtfmtConfig>?
    val ktlint: KtlintConfig?
}
