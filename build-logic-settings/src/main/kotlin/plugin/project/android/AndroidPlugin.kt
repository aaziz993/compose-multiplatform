@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.LibraryPlugin
import com.android.build.gradle.internal.services.DslServices
import com.android.build.gradle.internal.variant.DimensionCombinator
import gradle.android
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import org.gradle.api.Plugin
import org.gradle.api.Project
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
        android {
            buildTypes {
                create("some") {

                }
            }
            flavorDimensions("api", "tr")
            productFlavors {
                create("demo") {
                    dimension = "tr"
                }
                create("full") {
                    dimension = "api"
                }
                create("game") {
                    dimension = "api"
                }
            }
        }

        val variantInputModel = if (projectProperties.type == ProjectType.APP)
            plugins.findPlugin(AppPlugin::class.java)!!.variantInputModel
        else
            plugins.findPlugin(LibraryPlugin::class.java)!!.variantInputModel

        DimensionCombinator(
            variantInputModel,
            android::class.memberProperties.find { it.name == "dslServices" }!!.let {
                it.isAccessible = true
                it.getter.call(android) as DslServices
            }.issueReporter,
            android.flavorDimensionList,
        ).computeVariants().map {
            "${
                it.productFlavors
                    .joinToString("") { it.second.capitalized() }
            }${it.buildType.orEmpty().capitalized()}"
        }.forEach {
            println("ANDROID $it")
        }


        when (projectProperties.layout) {
            ProjectLayout.FLAT -> android.sourceSets.all {

//                kotlin.setSrcDirs(listOf("$compilationPrefixPart@android"))
//                java.setSrcDirs(listOf("$compilationPrefixPart@android"))
//                manifest.srcFile("$compilationPrefixPart@android/AndroidManifest.xml")
//                resources.setSrcDirs(listOf("${resourcesPrefixPart}Resources@android".decapitalized()))
//                res.setSrcDirs(listOf("${resourcesPrefixPart}Res@android".decapitalized()))
//                assets.setSrcDirs(listOf("${resourcesPrefixPart}Assets@android".decapitalized()))
//                shaders.setSrcDirs(listOf("${resourcesPrefixPart}Shaders@android".decapitalized()))
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
