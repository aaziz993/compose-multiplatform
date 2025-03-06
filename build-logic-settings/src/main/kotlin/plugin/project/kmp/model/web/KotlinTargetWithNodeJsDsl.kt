package plugin.project.kmp.model.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinTargetWithNodeJsDsl

internal interface KotlinTargetWithNodeJsDsl {

    val nodejs: KotlinJsNodeDsl?

    context(Project)
    fun applyTo(target: KotlinTargetWithNodeJsDsl) {
        nodejs?.let { nodejs ->
            target.nodejs {
                nodejs.applyTo(this)
            }
        }
    }
}
