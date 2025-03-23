package gradle.plugins.kmp.nat

import gradle.plugins.kmp.KotlinTargetTestRun
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestFilter
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests
import org.jetbrains.kotlin.gradle.targets.native.KotlinNativeBinaryTestRun

internal interface KotlinNativeBinaryTestRun<T : KotlinNativeBinaryTestRun, F : TestFilter> : KotlinTargetTestRun<T, F> {


}
