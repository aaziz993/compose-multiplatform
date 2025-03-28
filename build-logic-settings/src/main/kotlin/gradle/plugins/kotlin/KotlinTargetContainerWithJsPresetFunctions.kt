package gradle.plugins.kotlin

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.plugins.kotlin.targets.web.KotlinJsTarget
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithJsPresetFunctions
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

internal interface KotlinTargetContainerWithJsPresetFunctions<T : KotlinTargetContainerWithJsPresetFunctions> {

    val js: LinkedHashSet<KotlinJsTarget>?

    context(Project)
    @Suppress("UNCHECKED_CAST")
    fun applyTo(receiver: T) {
        js?.forEach { js ->
            js.applyTo(
                receiver.targets.matching { target ->
                    target::class == KotlinJsTargetDsl::class
                } as NamedDomainObjectCollection<KotlinJsTargetDsl>,
            ) { name, action ->
                receiver.js(name, action::execute)
            }
        }
    }
}
