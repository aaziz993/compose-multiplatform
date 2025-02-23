package plugin.project.web.js

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.web.configureJsTestTasks
import plugin.project.web.js.karakum.configureKarakumGenerate

internal class JsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.js?.forEach { targetName, target ->
                if (targetName.isEmpty()) {
                    kotlin.js(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.js {
                    target.applyTo(this)
                }
            } ?: return

            plugins.apply(settings.libs.plugins.plugin("karakum").id)

            configureKarakumGenerate()



            kotlin.sourceSets.matching { sourceSet -> sourceSet.name.startsWith("js") }.all {
                val karakumGeneratedDir = projectDir.resolve("src/jsMain/generated")

                if (karakumGeneratedDir.exists()) {
                    kotlin.srcDir(karakumGeneratedDir)
                }
            }

            configureJsTestTasks<KotlinJsTargetDsl>()
        }
    }
}

