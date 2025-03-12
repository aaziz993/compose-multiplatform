package gradle.plugins.kmp

import org.gradle.api.Named
import org.gradle.api.Project

internal abstract class KotlinTaskTestRun : KotlinTargetTestRun {

    context(Project)
    override fun applyTo(named: Named) = super.applyTo(named)
}
