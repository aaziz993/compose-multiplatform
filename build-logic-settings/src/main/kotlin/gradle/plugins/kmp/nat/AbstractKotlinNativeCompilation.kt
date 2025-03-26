package gradle.plugins.kmp.nat

import gradle.plugins.kmp.nat.tasks.KotlinNativeCompile
import gradle.plugins.kotlin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinNativeCompilation

internal abstract class AbstractKotlinNativeCompilation<T : AbstractKotlinNativeCompilation>
    : KotlinCompilation<T> {

    abstract override val compileTaskProvider: KotlinNativeCompile<out org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>?
}
