package gradle.plugins.kmp


import org.gradle.api.Named

internal abstract class KotlinTaskTestRun : KotlinTargetTestRun {

        context(Project)
    override fun applyTo(named: T) = super.applyTo(named)
}
