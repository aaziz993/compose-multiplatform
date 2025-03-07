@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.model

import gradle.model.kotlin.KotlinCommonCompilerOptions
import org.gradle.api.Named
import org.gradle.api.Project

/**
 * Analogous to [KotlinCompilationTask] for K2
 * This does not extend [KotlinCompilationTask], since [KotlinCompilationTask] carries an unwanted/conflicting
 * type parameter `<out T : KotlinCommonOptions>`
 */
internal interface K2MultiplatformCompilationTask : Task {

    val compilerOptions: KotlinCommonCompilerOptions?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.tasks.K2MultiplatformCompilationTask

        compilerOptions?.applyTo(named.compilerOptions)
    }
}
