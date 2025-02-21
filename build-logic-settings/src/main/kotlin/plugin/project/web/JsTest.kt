package plugin.project.web

import gradle.kotlin
import gradle.maybeNamed
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

internal inline fun <reified T : KotlinJsTargetDsl> Project.configureJsTestTasks() =
    kotlin.targets.withType<T> {
        val shouldRunJsBrowserTest = !hasProperty("teamcity") || hasProperty("enable-js-tests")
        if (shouldRunJsBrowserTest) return@withType

        tasks.maybeNamed("clean${targetName.capitalized()}BrowserTest") { onlyIf { false } }
        tasks.maybeNamed("${targetName}BrowserTest") { onlyIf { false } }
    }
