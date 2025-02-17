package plugin.project.gradle.kover.model

import kotlinx.kover.gradle.plugin.dsl.KoverVersions.JACOCO_TOOL_DEFAULT_VERSION

/**
 * Project extension for Kover Gradle Plugin.
 */
internal interface KoverExtension {

    /**
     * Specifies usage of [JaCoCo](https://www.jacoco.org/jacoco/) as coverage tool for measure coverage and generate reports.
     *
     * The version specified in the [jacocoVersion] will be used.
     */
    val useJacoco: Boolean?

    /**
     * Specifies version of [JaCoCo](https://www.jacoco.org/jacoco/) coverage tool.
     *
     * This property has an effect only if JaCoCo usage is enabled.
     *
     * [JACOCO_TOOL_DEFAULT_VERSION] by default.
     */
    val jacocoVersion: String?

    /**
     * Instance to configuring of report variants shared by the current project.
     *
     * See details in [currentProject].
     */
    val currentProject: KoverCurrentProjectVariantsConfig?

    /**
     * Instance to configuring of Kover reports.
     *
     * See details in [reports].
     */
    val reports: KoverReportsConfig?
}
