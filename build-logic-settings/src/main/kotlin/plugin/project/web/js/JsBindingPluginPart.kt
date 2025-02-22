package plugin.project.web.js

import gradle.kotlin
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.kotlin.model.target.TargetType
import plugin.project.kotlin.model.target.applyTo
import plugin.project.kotlin.model.target.contains
import plugin.project.web.configureJsTestTasks
import plugin.project.web.configureKotlinJsTarget
import plugin.project.web.js.karakum.configureKarakum

internal class JsBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.js == null) {
                return@with
            }

            projectProperties.kotlin.js!!.forEach { target ->
                target.applyTo()
            }

            configureJsTestTasks<KotlinJsTargetDsl>()
        }
    }
}

