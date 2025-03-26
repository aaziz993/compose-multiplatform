@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.android

import gradle.accessors.android
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.trySetSystemProperty
import gradle.decapitalized
import gradle.plugins.android.application.BaseAppModuleExtension
import gradle.plugins.android.library.LibraryExtension
import gradle.plugins.kmp.android.KotlinAndroidTarget
import gradle.prefixIfNotEmpty
import gradle.project.ProjectLayout
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

private val testSourceSetNamePrefixes = listOf(
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

            projectProperties.android?.let { android ->
                when (android) {
                    is LibraryExtension -> plugins.apply(project.settings.libs.plugins.plugin("androidLibrary").id)
                    is BaseAppModuleExtension -> plugins.apply(project.settings.libs.plugins.plugin("androidApplication").id)
                    else -> Unit
                }

                android.applyTo()
            }


            adjustAndroidSourceSets()
            applyGoogleServicesPlugin()

            afterEvaluate {
                adjustXmlFactories()
            }
        }
    }

    private fun Project.adjustAndroidSourceSets() =
        when (val layout = projectProperties.layout) {
            is ProjectLayout.Flat -> android.sourceSets.configureEach { sourceSet ->
                val (srcPrefixPart, resourcesPrefixPart) =
                    if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                    else testSourceSetNamePrefixes.find { prefix ->
                        sourceSet.name.startsWith(prefix)
                    }?.let { prefix ->
                        "$prefix${sourceSet.name.removePrefix(prefix).prefixIfNotEmpty("+")}".let { it to it }
                    } ?: sourceSet.name.let { it to it }

                sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPrefixPart${layout.targetDelimiter}android")
                sourceSet.resources.replace("src/${sourceSet.name}/resources", "${resourcesPrefixPart}Resources${layout.targetDelimiter}android".decapitalized())
                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart${layout.targetDelimiter}android")
                sourceSet.manifest.srcFile("$srcPrefixPart${layout.targetDelimiter}android/AndroidManifest.xml")
                sourceSet.res.replace("src/${sourceSet.name}/res", "${resourcesPrefixPart}Res${layout.targetDelimiter}android".decapitalized())
                sourceSet.assets.replace("src/${sourceSet.name}/assets", "${resourcesPrefixPart}Assets${layout.targetDelimiter}android".decapitalized())
                sourceSet.aidl.replace("src/${sourceSet.name}/aidl", "${resourcesPrefixPart}Aidl${layout.targetDelimiter}android".decapitalized())
                sourceSet.renderscript.replace("src/${sourceSet.name}/rs", "${resourcesPrefixPart}Rs${layout.targetDelimiter}android".decapitalized())
                sourceSet.jniLibs.replace("src/${sourceSet.name}/jniLibs", "${resourcesPrefixPart}JniLibs${layout.targetDelimiter}android".decapitalized())
                sourceSet.shaders.replace("src/${sourceSet.name}/shaders", "${resourcesPrefixPart}Shaders${layout.targetDelimiter}android".decapitalized())
                sourceSet.mlModels.replace("src/${sourceSet.name}/mlModels", "${resourcesPrefixPart}MlModels${layout.targetDelimiter}android".decapitalized())
            }

            else -> Unit
        }

    private fun Project.applyGoogleServicesPlugin() {
        if (file("google-services.json").exists()) {
            plugins.apply(project.settings.libs.plugins.plugin("google.playServices").id)
        }
    }

    /**
     * W/A for service loading conflict between apple plugin
     * and android plugin.
     */
    private fun adjustXmlFactories() {
        trySetSystemProperty(
            XMLInputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLInputFactoryImpl",
        )
        trySetSystemProperty(
            XMLOutputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLOutputFactoryImpl",
        )
        trySetSystemProperty(
            XMLEventFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
        )
    }
}
