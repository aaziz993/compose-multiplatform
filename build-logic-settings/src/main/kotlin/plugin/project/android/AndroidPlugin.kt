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
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies
import gradle.model.kotlin.kmp.jvm.android.KotlinAndroidTarget
import gradle.model.kotlin.sourceSets
import gradle.model.project.ProjectLayout
import gradle.model.project.ProjectType

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

//            projectProperties.android?.applyTo()

            if (!projectProperties.kotlin.enabledKMP) {
                projectProperties.kotlin.sourceSets<KotlinAndroidTarget>()?.forEach { sourceSet ->
                    val compilationName = if (
                        sourceSet.name == "commonTest" ||
                        sourceSet.name.startsWith(SourceSet.TEST_SOURCE_SET_NAME) ||
                        sourceSet.name.startsWith("androidTest") ||
                        sourceSet.name.startsWith("testFixtures")
                    ) "test"
                    else ""

                    dependencies {
                        sourceSet.dependencies
                            ?.forEach { dependency ->
                                add(
                                    "$compilationName${dependency.configuration.capitalized()}"
                                        .decapitalized(),
                                    dependency.resolve(),
                                )
                            }
                    }
                }
            }

            adjustAndroidSourceSets()
            applyGoogleServicesPlugin()
        }
    }

    private fun Project.adjustAndroidSourceSets() =
        when (projectProperties.layout) {
            ProjectLayout.FLAT -> android.sourceSets.all { sourceSet ->
                val (srcPrefixPart, resourcesPrefixPart) = when {
                    sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME -> "src" to ""

                    sourceSet.name.startsWith(SourceSet.TEST_SOURCE_SET_NAME) -> "${SourceSet.TEST_SOURCE_SET_NAME}${
                        sourceSet.name.removePrefix(SourceSet.TEST_SOURCE_SET_NAME).prefixIfNotEmpty("+")
                    }".let { it to it }

                    sourceSet.name.startsWith("androidTest") -> "androidTest${
                        sourceSet.name.removePrefix("androidTest").prefixIfNotEmpty("+")
                    }".let { it to it }

                    sourceSet.name.startsWith("testFixtures") -> "testFixtures${
                        sourceSet.name.removePrefix("testFixtures").prefixIfNotEmpty("+")
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

    private fun Project.applyGoogleServicesPlugin() {
        if (file("google-services.json").exists()) {
            plugins.apply(settings.libs.plugins.plugin("google.playServices").id)
        }
    }
}
