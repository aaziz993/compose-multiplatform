package plugin.project.web

import gradle.accessors.id
import gradle.accessors.karakum
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.plugins.kmp.web.KotlinJsTarget
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.serialization.decodeMapFromString
import gradle.accessors.settings
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

internal class JsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinJsTarget }) {
                return@with
            }

            plugins.apply(settings.libs.plugins.plugin("karakum").id)

            projectProperties.karakum.applyTo()

            val karakumConfigFile = karakum.configFile.asFile.get()

            if (karakumConfigFile.exists()) {

                val karakumConfig = Json.Default.decodeMapFromString(karakumConfigFile.readText())

                val karakumOutputDir = karakumConfigFile.parentFile.resolve(karakumConfig["output"].toString())

                if (karakumOutputDir.exists()) {
                    kotlin.sourceSets.matching { sourceSet -> sourceSet.name == "jsMain" }.all {

                        kotlin.srcDir(karakumOutputDir)
                    }
                }
            }

            configureJsTestTasks<KotlinJsTargetDsl>()
        }
    }
}
