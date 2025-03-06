package plugin.project.kmp.model.nat

import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun
import plugin.project.kmp.model.KotlinTargetTestRun

internal interface KotlinNativeBinaryTestRun : KotlinTargetTestRun<NativeBinaryTestRunSource> {

    context(Project)
    override fun applyTo(named: Named) {
        named as KotlinNativeBinaryTestRun

        executionSource?.applyTo(named.executionSource)
    }
}
