package gradle.model.kmp.nat

import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun
import gradle.model.kmp.KotlinTargetTestRun

internal interface KotlinNativeBinaryTestRun : KotlinTargetTestRun<NativeBinaryTestRunSource> {

    context(Project)
    override fun applyTo(named: Named) {
        named as KotlinNativeBinaryTestRun

        executionSource?.applyTo(named.executionSource)
    }
}
