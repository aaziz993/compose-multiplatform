package plugin.project.web.js

import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains
import plugin.project.web.configureJsTestTasks
import plugin.project.web.configureKotlinJsTarget
import plugin.project.web.js.karakum.configureKarakum

internal class JsBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.JS !in moduleProperties.targets) {
                return@with
            }

            moduleProperties.targets
                .filter { target -> target.type.isDescendantOf(TargetType.JS) }
                .forEach { target -> target.add() }

            configureKotlinJsTarget<KotlinJsTargetDsl>()
            configureJsTestTasks<KotlinJsTargetDsl>()
            configureKarakum()
        }
    }
}

