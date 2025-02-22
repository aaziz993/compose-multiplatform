package plugin.project.kotlin.model.language.web

import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsNodeDsl

internal interface KotlinTargetWithNodeJsDsl {

    val nodejs: KotlinJsNodeDsl?
}
