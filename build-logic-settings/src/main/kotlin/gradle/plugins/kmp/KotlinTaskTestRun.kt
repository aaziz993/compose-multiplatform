package gradle.plugins.kmp

import org.jetbrains.kotlin.gradle.plugin.KotlinExecution
import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

internal abstract class KotlinTaskTestRun<T : KotlinTaskTestRun<E, *>, E : KotlinExecution.ExecutionSource>
    : KotlinTargetTestRun<E, T> {

}
