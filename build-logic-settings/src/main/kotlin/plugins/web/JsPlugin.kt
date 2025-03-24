package plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinJsTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

internal class JsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinJsTarget }) {
                return@with
            }

            configureJsTestTasks<KotlinJsTargetDsl>()
        }
    }
}
