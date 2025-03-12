package gradle.plugins.kover

import gradle.kover
import gradle.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverVersions.JACOCO_TOOL_DEFAULT_VERSION
import org.gradle.api.Project

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

    context(Project)
    fun applyTo() {
        kover.useJacoco tryAssign useJacoco
        kover.jacocoVersion tryAssign jacocoVersion

        currentProject?.let { currentProject ->
            kover.currentProject(currentProject::applyTo)
        }

        reports?.let { reports ->
            kover.reports {
                reports.applyTo(this)
            }
        }
    }
}
