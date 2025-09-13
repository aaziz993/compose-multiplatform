package gradle.plugins.dokka

import gradle.api.configureEach
import gradle.api.project.ProjectLayout
import gradle.api.project.dokka
import gradle.api.project.projectScript
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign

public class DokkaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            adjustSourceLinks()
        }
    }

    private fun Project.adjustSourceLinks() {
        dokka.dokkaSourceSets.configureEach { sourceSet ->
            sourceSet.sourceLinks.configureEach { sourceLink ->
                sourceLink.localDirectory = layout.projectDirectory.dir(
                    when (projectScript.layout) {
                        is ProjectLayout.Flat -> ""
                        else -> "src"
                    },
                )
            }
        }
    }
}
