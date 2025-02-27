@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.variant.DimensionCombinator
import gradle.android
import gradle.decapitalized
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import plugin.project.kotlin.model.KotlinAndroidTarget
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinAndroidTarget } != false) {
                return@with
            }

            when (projectProperties.type) {
                ProjectType.APP -> plugins.apply(settings.libs.plugins.plugin("androidApplication").id)

                else -> plugins.apply(settings.libs.plugins.plugin("androidLibrary").id)
            }

//            configureBaseExtension()

            adjustAndroidSourceSets()
            applyGoogleServicesPlugin()
        }
    }

    fun <T> List<List<T>>.combinations(): Sequence<List<T>> {
        if (isEmpty()) return sequenceOf(emptyList())

        val firstList = first()
        val restCombinations = drop(1).combinations()

        return sequence {
            for (item in firstList) {
                for (combination in restCombinations) {
                    yield(listOf(item) + combination)
                }
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun Project.adjustAndroidSourceSets() {
//        android {
//            buildTypes {
//                create("some") {
//
//                }
//            }
//            flavorDimensions("api", "tr")
//            productFlavors {
//                create("demo") {
//                    dimension = "tr"
//                }
//                create("full")
//                create("game") {
//                    dimension = "api"
//                }
//            }
//        }

//        DimensionCombinator(android.,
//
//            ).computeVariants().map {
//
//        }

        when (projectProperties.layout) {
            ProjectLayout.FLAT -> android.sourceSets.all {
                val isMain = name == SourceSet.MAIN_SOURCE_SET_NAME
                val buildType = android.buildTypes.find { name.contains(it.name, ignoreCase = true) }?.name
                val productFlavor = android.productFlavors.find { name.contains(it.name, ignoreCase = true) }?.name
                val isTest = name.contains("androidTest", ignoreCase = true) || name.contains("test", ignoreCase = true)

                val sourceSetNameParts = "^.*?(Main|Test|TestDebug)?$".toRegex().matchEntire(name)!!

                val (compilationPrefixPart, resourcesPrefixPart) = sourceSetNameParts.groupValues[1]
                    .decapitalized()
                    .let { compilationName ->
                        when (compilationName) {
                            "main", "" -> "src" to ""

                            else -> compilationName to compilationName
                        }
                    }

                kotlin.setSrcDirs(listOf("$compilationPrefixPart@android"))
                java.setSrcDirs(listOf("$compilationPrefixPart@android"))
                manifest.srcFile("$compilationPrefixPart@android/AndroidManifest.xml")
                resources.setSrcDirs(listOf("${resourcesPrefixPart}Resources@android".decapitalized()))
                res.setSrcDirs(listOf("${resourcesPrefixPart}Res@android".decapitalized()))
                assets.setSrcDirs(listOf("${resourcesPrefixPart}Assets@android".decapitalized()))
                shaders.setSrcDirs(listOf("${resourcesPrefixPart}Shaders@android".decapitalized()))
            }

            else -> Unit
        }
    }

    private fun Project.applyGoogleServicesPlugin() {
        if (file("google-services.json").exists()) {
            plugins.apply(settings.libs.plugins.plugin("google.playServices").id)
        }
    }
}
