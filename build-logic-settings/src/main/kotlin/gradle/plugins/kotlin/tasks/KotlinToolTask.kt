package gradle.plugins.kotlin.tasks

import org.gradle.kotlin.dsl.withType
import gradle.plugins.kotlin.KotlinCommonCompilerToolOptions
import gradle.tasks.Task
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinToolTask

/**
 * Represents a Kotlin task performing further processing of compiled code via additional Kotlin tools using configurable [toolOptions].
 *
 * Check [KotlinCommonCompilerToolOptions] inheritors (excluding [KotlinCommonCompilerToolOptions]) for the possible configuration
 * options.
 *
 * @see [KotlinCommonCompilerToolOptions]
 */
internal interface KotlinToolTask<out TO : KotlinCommonCompilerToolOptions> : Task {

    /**
     * Represents the tool options used by a Kotlin task with reasonable configured defaults.
     *
     * Could be used to either get the values of currently configured options or to modify them.
     */
    val toolOptions: TO?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinToolTask<*>

        toolOptions?.applyTo(named.toolOptions)
    }

    context(Project)
    override fun applyTo() =
        super.applyTo(tasks.withType<KotlinToolTask<*>>())
}
