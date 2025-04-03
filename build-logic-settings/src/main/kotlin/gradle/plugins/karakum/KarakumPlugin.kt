package gradle.plugins.karakum

import gradle.accessors.kotlin
import gradle.api.project.projectProperties
import gradle.plugins.kotlin.KotlinTarget
import gradle.plugins.kotlin.targets.web.KotlinJsTarget
import klib.data.type.serialization.decodeMapFromString
import java.io.File
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KarakumPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Apply karakum properties.
            projectProperties.karakum?.let { karakum ->
                karakum.applyTo()

                project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
                    karakum.configFile
                        ?.let(project::file)
                        ?.takeIf(File::exists)
                        ?.let(File::readText)
                        ?.let(Json.Default::decodeMapFromString)
                        ?.let { karakumConfig ->

                            val karakumOutputDir = file(karakumConfig["output"]!!)

                            val targetNames = projectProperties.kotlin?.targets.orEmpty()
                                .filterIsInstance<KotlinJsTarget>()
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
}
