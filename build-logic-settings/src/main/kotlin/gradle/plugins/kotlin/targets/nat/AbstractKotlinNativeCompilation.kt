package gradle.plugins.kotlin.targets.nat

import gradle.plugins.kotlin.KotlinCompilation
import gradle.plugins.kotlin.targets.nat.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinNativeCompilation

internal abstract class AbstractKotlinNativeCompilation<T : AbstractKotlinNativeCompilation>
    : KotlinCompilation<T> {

    abstract override val compileTaskProvider: KotlinNativeCompile<out org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>?
}
