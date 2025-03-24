package gradle.plugins.kmp.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinTargetWithNodeJsDsl

internal interface KotlinTargetWithNodeJsDsl {

    val nodejs: KotlinJsNodeDsl?

    context(project: Project)
    fun applyTo(recipient: KotlinTargetWithNodeJsDsl, distributionName: String) {
        nodejs?.let { nodejs ->
            recipient.nodejs {
                nodejs.applyTo(this, distributionName)
            }
        }
    }
}
