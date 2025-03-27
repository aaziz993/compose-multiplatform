package gradle.plugins.kotlin.mpp

import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinTarget

internal abstract class AbstractKotlinTarget<T : AbstractKotlinTarget>
    : InternalKotlinTarget<T> {

}
