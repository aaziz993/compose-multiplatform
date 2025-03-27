package gradle.plugins.kotlin.mpp

import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget

internal abstract class KotlinOnlyTarget<T : KotlinOnlyTarget<C>, C : KotlinCompilation<*>>
    : AbstractKotlinTarget<T>() {

}
