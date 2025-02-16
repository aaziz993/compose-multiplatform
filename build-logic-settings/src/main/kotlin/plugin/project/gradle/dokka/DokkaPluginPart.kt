package plugin.project.gradle.dokka

import gradle.amperModuleExtraProperties
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.settings
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask

internal class DokkaPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val spotless by lazy {
        project.amperModuleExtraProperties.settings.gradle.spotless
    }

    override val needToApply: Boolean by lazy {
        spotless.enabled
    }

    override fun applyBeforeEvaluate() {
        super.applyAfterEvaluate()

        project.plugins.apply(project.settings.libs.plugins.plugin("dokka").id)

        applySettings()
    }

    fun applySettings() = with(project) {
        configureDokkaExtension()

        val dokkaPlugin by configurations

        dependencies {
            dokkaPlugin(libs.dokka.versioning)
        }
    }

    private fun Project.configureDokkaMultiModuleTask(task: DokkaMultiModuleTask) =
        tasks.withType<DokkaMultiModuleTask> {
            val version = project.version
            val dokkaOutputDir = "../versions"
            val id = "org.jetbrains.dokka.versioning.VersioningPlugin"
            val config = """{ "version": "$version", "olderVersionsDir":"$dokkaOutputDir" }"""

            outputDirectory = project.layout.projectDirectory.dir("$dokkaOutputDir/$version")
            pluginsMapConfiguration = mapOf(id to config)
        }
}
