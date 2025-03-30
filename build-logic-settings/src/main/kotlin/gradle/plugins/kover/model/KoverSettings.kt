package gradle.plugins.kover.model

import gradle.plugins.kover.KoverExtension
import gradle.plugins.kover.currentproject.KoverCurrentProjectVariantsConfig
import gradle.plugins.kover.reports.KoverReportsConfig
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

@Serializable
internal data class KoverSettings(
    override val useJacoco: Boolean? = null,
    override val jacocoVersion: String? = null,
    override val currentProject: KoverCurrentProjectVariantsConfig? = null,
    override val reports: KoverReportsConfig? = null,
    val dependenciesFromSubprojects: Boolean = true,
) : KoverExtension {

    context(Project)
    override fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
            super.applyTo()

            if (dependenciesFromSubprojects) {
                val kover by configurations
                project.dependencies {
                    subprojects.forEach { subproject ->
                        kover(subproject)
                    }
                }
            }
        }
}
