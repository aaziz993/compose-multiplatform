package gradle.plugins.kmp

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget

internal abstract class KotlinOnlyTarget<T : KotlinOnlyTarget<C>, C : org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<*>>
    : AbstractKotlinTarget<T>() {

}
