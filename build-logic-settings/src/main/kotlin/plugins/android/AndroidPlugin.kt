@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugins.android

import gradle.accessors.android
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.all
import gradle.decapitalized
import gradle.file.replace
import gradle.plugins.kmp.android.KotlinAndroidTarget
import gradle.plugins.kotlin.sourceSets
import gradle.plugins.project.ProjectLayout
import gradle.plugins.project.ProjectType
import gradle.prefixIfNotEmpty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies

private val androidSourceSetNamePrefixes = listOf(
    SourceSet.TEST_SOURCE_SET_NAME,
    "androidTest",
    "testFixtures",
)

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinAndroidTarget }) {
                return@with
            }

            if (projectProperties.type == ProjectType.APP)
                plugins.apply(settings.libs.plugins.plugin("androidApplication").id)
            else
                plugins.apply(settings.libs.plugins.plugin("androidLibrary").id)

            projectProperties.android?.applyTo()

            if (!projectProperties.kotlin.enabledKMP) {
                projectProperties.kotlin.sourceSets<KotlinAndroidTarget>()?.forEach { sourceSet ->
                    val compilationName = if (
                        sourceSet.name == "commonTest" ||
                        androidSourceSetNamePrefixes.any(sourceSet.name::startsWith)
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
                val (srcPrefixPart, resourcesPrefixPart) =
                    if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                    else androidSourceSetNamePrefixes.find { prefix ->
                        sourceSet.name.startsWith(prefix)
                    }?.let { prefix ->
                        "$prefix${sourceSet.name.removePrefix(prefix).prefixIfNotEmpty("+")}".let { it to it }
                    } ?: sourceSet.name.let { it to it }

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
