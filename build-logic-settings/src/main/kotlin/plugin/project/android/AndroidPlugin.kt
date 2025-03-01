@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.android

import gradle.all
import gradle.android
import gradle.decapitalized
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.prefixIfNotEmpty
import gradle.projectProperties
import gradle.replace
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import plugin.project.kotlin.kmp.model.android.KotlinAndroidTarget
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



        when (projectProperties.layout) {

            ProjectLayout.FLAT -> android.sourceSets.all { sourceSet ->
                val (srcPrefixPart, resourcesPrefixPart) = when (sourceSet.name) {
                    SourceSet.MAIN_SOURCE_SET_NAME -> "src" to ""

                    SourceSet.TEST_SOURCE_SET_NAME -> "${SourceSet.TEST_SOURCE_SET_NAME}${
                        sourceSet.name.removePrefix(SourceSet.TEST_SOURCE_SET_NAME).prefixIfNotEmpty("+")
                    }".let { it to it }

                    "androidTest" -> "androidTest${
                        sourceSet.name.removePrefix("androidTest").prefixIfNotEmpty("+")
                    }".let { it to it }

                    "textFixtures" -> "textFixtures${
                        sourceSet.name.removePrefix("textFixtures").prefixIfNotEmpty("+")
                    }".let { it to it }

                    else -> sourceSet.name.let { it to it }
                }

                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart@android")
                sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPrefixPart@android")
                sourceSet.manifest.srcFile("$srcPrefixPart@android/AndroidManifest.xml")
                sourceSet.res.replace("src/${sourceSet.name}/res", "${resourcesPrefixPart}Res@android".decapitalized())
                sourceSet.assets.replace("src/${sourceSet.name}/assets", "${resourcesPrefixPart}Assets@android".decapitalized())
                sourceSet.aidl.replace("src/${sourceSet.name}/aidl", "${resourcesPrefixPart}Aidl@android".decapitalized())
                sourceSet.renderscript.replace("src/${sourceSet.name}/rs", "${resourcesPrefixPart}Rs@android".decapitalized())
                sourceSet.jniLibs.replace("src/${sourceSet.name}/jniLibs", "${resourcesPrefixPart}JniLibs@android".decapitalized())
                sourceSet.resources.replace("src/${sourceSet.name}/resources", "${resourcesPrefixPart}Resources@android".decapitalized())
                sourceSet.shaders.replace("src/${sourceSet.name}/shaders", "${resourcesPrefixPart}Shaders@android".decapitalized())

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
