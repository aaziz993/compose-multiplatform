package gradle.plugins.kotlin.testing

import gradle.plugins.kotlin.KotlinTargetTestRun
import org.jetbrains.kotlin.gradle.plugin.KotlinExecution
import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

internal abstract class KotlinTaskTestRun<T : KotlinTaskTestRun<E, *>, E : KotlinExecution.ExecutionSource>
    : KotlinTargetTestRun<E, T> {

}
