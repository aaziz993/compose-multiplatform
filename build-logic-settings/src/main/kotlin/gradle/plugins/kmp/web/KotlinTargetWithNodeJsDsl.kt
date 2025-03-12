package gradle.plugins.kmp.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinTargetWithNodeJsDsl

internal interface KotlinTargetWithNodeJsDsl {

    val nodejs: KotlinJsNodeDsl?

    context(Project)
    fun applyTo(target: KotlinTargetWithNodeJsDsl, distributionName: String) {
        nodejs?.let { nodejs ->
            target.nodejs {
                nodejs.applyTo(this, distributionName)
            }
        }
    }
}
