package plugin.project.web

import gradle.kotlin
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

@OptIn(ExperimentalMainFunctionArgumentsDsl::class, ExperimentalDistributionDsl::class)
internal inline fun <reified T : KotlinJsTargetDsl> Project.configureKotlinJsTarget() =
    moduleProperties.settings.web.let { web ->
        kotlin.targets.withType<T> {
            web.applyTo(this)
        }
    }


