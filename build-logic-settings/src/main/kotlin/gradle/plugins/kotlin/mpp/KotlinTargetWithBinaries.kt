package gradle.plugins.kotlin.mpp

import org.gradle.api.DomainObjectSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinTargetWithBinaries

internal abstract class KotlinTargetWithBinaries<
    T : KotlinTargetWithBinaries<C, R>,
    C : KotlinCompilation<*>,
    out R : DomainObjectSet<*>,
    S : Set<*>>
    : KotlinOnlyTarget<T, C>(), HasBinaries<S> {

}
