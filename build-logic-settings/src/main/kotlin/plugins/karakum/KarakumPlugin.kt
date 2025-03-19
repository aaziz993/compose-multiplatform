package plugins.karakum

import gradle.accessors.id
import gradle.accessors.karakum
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kmp.KotlinTarget
import gradle.plugins.kmp.web.KotlinJsTarget
import gradle.serialization.decodeMapFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KarakumPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.karakum
                .takeIf { it.enabled && projectProperties.kotlin.targets.any { target -> target is KotlinJsTarget } }
                ?.let { karakum ->
                    plugins.apply(settings.libs.plugins.plugin("karakum").id)

                    karakum.applyTo()

                    val karakumConfigFile = project.karakum.configFile.asFile.get()

                    if (karakumConfigFile.exists()) {

                        val karakumConfig = Json.Default.decodeMapFromString(karakumConfigFile.readText())

                        val karakumOutputDir = karakumConfigFile.parentFile.resolve(karakumConfig["output"].toString())

                        val targetNames = projectProperties.kotlin.targets
                            .filter { target -> target is KotlinJsTarget }
                            .map(KotlinTarget::targetName)

                        kotlin.sourceSets.matching { sourceSet ->
                            targetNames.any { targetName ->
                                sourceSet.name == "${targetName}Main" || sourceSet.name == "${targetName}Test"
                            }
                        }.all {
                            kotlin.srcDir(karakumOutputDir)
                        }
                    }
                }
        }
    }
}
