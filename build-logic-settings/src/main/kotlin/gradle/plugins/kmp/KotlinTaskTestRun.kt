package gradle.plugins.kmp

import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

internal abstract class KotlinTaskTestRun<T : KotlinTaskTestRun<*, *>> : KotlinTargetTestRun<T> {

}
