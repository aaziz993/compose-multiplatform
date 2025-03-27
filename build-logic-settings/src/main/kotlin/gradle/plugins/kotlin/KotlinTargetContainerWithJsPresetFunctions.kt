package gradle.plugins.kotlin

import gradle.plugins.kmp.web.KotlinJsTarget
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithJsPresetFunctions

internal interface KotlinTargetContainerWithJsPresetFunctions<T : KotlinTargetContainerWithJsPresetFunctions> {

    val js: LinkedHashSet<KotlinJsTarget>?

    context(Project)
    fun applyTo(receiver: T) {
        js?.forEach { js ->
            receiver.js(js.name) {
                js.applyTo(this)
            }
        }
    }
}
