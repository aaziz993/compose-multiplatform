package gradle.plugins.kmp.nat

import gradle.plugins.kmp.KotlinTargetTestRun
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun

internal interface KotlinNativeBinaryTestRun : KotlinTargetTestRun {

    context(Project)
    override fun applyTo(named: Named) {
        named as KotlinNativeBinaryTestRun

        val target = named.target as KotlinNativeTargetWithTests<*>

        (executionSource as NativeBinaryTestRunSource?)?.let { executionSource ->
            named.setExecutionSourceFrom(
                target.binaries.getTest(executionSource.binary),
            )
        }
    }
}
