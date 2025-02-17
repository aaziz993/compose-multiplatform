package plugin.project.web

import gradle.maybeNamed
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

internal fun Project.configureJsTestTasks(target: KotlinJsTargetDsl) {
    val shouldRunJsBrowserTest = !hasProperty("teamcity") || hasProperty("enable-js-tests")
    if (shouldRunJsBrowserTest) return

    tasks.maybeNamed("clean${target.targetName.capitalized()}BrowserTest") { onlyIf { false } }
    tasks.maybeNamed("${target.targetName}BrowserTest") { onlyIf { false } }
}
