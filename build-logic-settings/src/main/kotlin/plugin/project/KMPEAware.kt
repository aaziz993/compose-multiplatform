package plugin.project

import org.jetbrains.amper.frontend.FragmentDependencyType
import org.jetbrains.amper.frontend.LeafFragment
import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.FragmentWrapper
import org.jetbrains.amper.gradle.LeafFragmentWrapper
import org.jetbrains.amper.gradle.kmpp.KotlinAmperNamingConvention.compilation
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal interface KMPEAware {
    val kotlinMPE: KotlinMultiplatformExtension

    val Platform.targetName
        get() = pretty

    val LeafFragmentWrapper.targetName
        get() = platform.targetName

    val LeafFragmentWrapper.target
        get() = kotlinMPE.targets.findByName(targetName)

    val Platform.target
        get() = kotlinMPE.targets.findByName(targetName)

    private val FragmentWrapper.commonKotlinSourceSetName: String
        get() = when {
            !isTest -> "commonMain"
            isTest -> "commonTest"
            else -> name
        }

    val FragmentWrapper.kotlinSourceSetName: String
        get() = when {
            !isTest && name == module.rootFragment.name -> "commonMain"
            // TODO Add variants support.
            !isTest && name == "android" -> "androidMain"
            isTest && name == "androidTest" -> "androidUnitTest"
            isTest && name == module.rootTestFragment.name -> "commonTest"
            else -> name
        }

    val FragmentWrapper.kotlinSourceSet: KotlinSourceSet?
        get() = kotlinMPE.sourceSets.findByName(kotlinSourceSetName)

    val FragmentWrapper.matchingKotlinSourceSets: List<KotlinSourceSet>
        get() = buildList {
            if (fragmentDependencies.none { it.type == FragmentDependencyType.REFINE }) {
                kotlinMPE.sourceSets.findByName(commonKotlinSourceSetName)?.let { add(it) }
            }
            kotlinMPE.sourceSets.findByName(kotlinSourceSetName)?.let { add(it) }
        }

    val LeafFragmentWrapper.targetCompilation: KotlinCompilation<KotlinCommonOptions>?
        get() = target?.run { (this@targetCompilation as LeafFragment).compilation }

    fun KotlinSourceSet.doDependsOn(it: FragmentWrapper) {
        val dependency = it.kotlinSourceSet
        dependsOn(dependency ?: return)
    }
}




