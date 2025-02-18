package plugin.project.web.js

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.assign

internal fun Project.configureKotlin2JsCompileTask() =
    tasks.withType<Kotlin2JsCompile>().configureEach {
        compilerOptions {
            target = "es2015"

            freeCompilerArgs.addAll(
                "-Xdont-warn-on-error-suppression",
                "-Xir-generate-inline-anonymous-functions", //  https://youtrack.jetbrains.com/issue/KT-67355
                "-Xsuppress-warning=NOTHING_TO_INLINE",
            )
        }
    }
