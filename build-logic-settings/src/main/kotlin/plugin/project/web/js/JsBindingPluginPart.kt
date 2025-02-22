package plugin.project.web.js

import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.web.configureJsTestTasks

internal class JsBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.js?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.js(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.js {
                    target.applyTo(this)
                }
            } ?: return

            configureJsTestTasks<KotlinJsTargetDsl>()
        }
    }
}

