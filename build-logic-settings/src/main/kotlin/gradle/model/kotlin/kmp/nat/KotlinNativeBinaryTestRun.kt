package gradle.model.kotlin.kmp.nat

import gradle.model.kotlin.kmp.KotlinTargetTestRun
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun

internal interface KotlinNativeBinaryTestRun : KotlinTargetTestRun<NativeBinaryTestRunSource> {

    /**
     * Sets this test run to use the specified [testExecutable].
     *
     * This overrides other [executionSource] options.
     */
    val executionSourceFrom: NativeBuildType?

    context(Project)
    override fun applyTo(named: Named) {
        named as KotlinNativeBinaryTestRun

        executionSource?.applyTo(named.executionSource)

        val target = named.target as KotlinNativeTargetWithTests<*>

        executionSourceFrom?.let(target.binaries::getTest)?.let(named::setExecutionSourceFrom)
    }
}
