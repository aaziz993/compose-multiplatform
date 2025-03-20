package gradle.plugins.kmp


import gradle.plugins.kotlin.KotlinTestRun
import org.gradle.api.Named

/**
 * A [KotlinTargetExecution] that executes configured tests in the context of a specific [KotlinTarget].
 */
internal interface KotlinTargetTestRun : KotlinTestRun, KotlinTargetExecution {

        context(Project)
    override fun applyTo(recipient: T) =
        super<KotlinTestRun>.applyTo(named as org.jetbrains.kotlin.gradle.plugin.KotlinTestRun<*>)
}
