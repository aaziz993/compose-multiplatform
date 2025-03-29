package gradle.plugins.karakum

import gradle.accessors.catalog.libs
import gradle.accessors.kotlin
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.karakum.model.KarakumSettings
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.targets.web.KotlinJsTarget
import gradle.serialization.decodeMapFromString
import java.io.File
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KarakumPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.karakum
                .takeIf(KarakumSettings::enabled)?.let { karakum ->
                    plugins.apply(project.settings.libs.plugin("karakum").id)

                    karakum.applyTo()

                    karakum.configFile
                        ?.let(project::file)
                        ?.takeIf(File::exists)
                        ?.let(File::readText)
                        ?.let(Json.Default::decodeMapFromString)
                        ?.let { karakumConfig ->

                            val karakumOutputDir = file(karakumConfig["output"]!!)

                            val targetNames = projectProperties.kotlin.targets
                                .filter { target -> target is KotlinJsTarget }
                                .map(KotlinTarget<*>::targetName)

                            kotlin.sourceSets.matching { sourceSet ->
                                targetNames.any { targetName ->
                                    sourceSet.name == "${targetName}Main" || sourceSet.name == "${targetName}Test"
                                }
                            }.configureEach {
                                kotlin.srcDir(karakumOutputDir)
                            }
                        }
                }
        }
    }
}
