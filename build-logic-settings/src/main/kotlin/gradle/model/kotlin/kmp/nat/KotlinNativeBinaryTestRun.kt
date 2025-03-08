package gradle.model.kotlin.kmp.nat

import gradle.model.kotlin.kmp.KotlinTargetTestRun
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun

internal interface KotlinNativeBinaryTestRun : KotlinTargetTestRun<NativeBinaryTestRunSource> {

    context(Project)
    override fun applyTo(named: Named) {
        named as KotlinNativeBinaryTestRun

        executionSource?.applyTo(named.executionSource)
    }
}
