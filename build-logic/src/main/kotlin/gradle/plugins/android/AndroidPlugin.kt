package gradle.plugins.android

import gradle.api.project.projectProperties

import gradle.api.project.ProjectLayout
import gradle.api.project.android
import klib.data.type.trySetSystemProperty
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import gradle.api.configureEach
import gradle.api.file.replace
import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.lowercaseFirst

private val TEST_SOURCE_SET_NAME_PREFIXES = listOf(
    SourceSet.TEST_SOURCE_SET_NAME,
    "androidTest",
    "testFixtures",
)

internal class AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            project.pluginManager.withPlugin("com.android.application") {
                adjustSourceSets()
                applyGoogleServicesPlugin()
                adjustXmlFactories()
            }
        }
    }

    private fun Project.adjustSourceSets() =
        when (val layout = projectProperties.layout) {
            is ProjectLayout.Flat -> android.sourceSets.configureEach { sourceSet ->
                val (srcPrefixPart, resourcesPrefixPart) =
                    if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                    else (TEST_SOURCE_SET_NAME_PREFIXES.find { prefix ->
                        sourceSet.name.startsWith(prefix)
                    }?.let { prefix ->
                        "$prefix${
                            sourceSet.name.removePrefix(prefix).addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                        }"
                    } ?: sourceSet.name).pair()

                sourceSet.kotlin.replace(
                    "src/${sourceSet.name}/kotlin",
                    "$srcPrefixPart${layout.targetDelimiter}android"
                )
                sourceSet.resources.replace(
                    "src/${sourceSet.name}/resources",
                    "${resourcesPrefixPart}Resources${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart${layout.targetDelimiter}android")
                sourceSet.manifest.srcFile("$srcPrefixPart${layout.targetDelimiter}android/AndroidManifest.xml")
                sourceSet.res.replace(
                    "src/${sourceSet.name}/res",
                    "${resourcesPrefixPart}Res${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.assets.replace(
                    "src/${sourceSet.name}/assets",
                    "${resourcesPrefixPart}Assets${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.aidl.replace(
                    "src/${sourceSet.name}/aidl",
                    "${resourcesPrefixPart}Aidl${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.renderscript.replace(
                    "src/${sourceSet.name}/rs",
                    "${resourcesPrefixPart}Rs${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.jniLibs.replace(
                    "src/${sourceSet.name}/jniLibs",
                    "${resourcesPrefixPart}JniLibs${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.shaders.replace(
                    "src/${sourceSet.name}/shaders",
                    "${resourcesPrefixPart}Shaders${layout.targetDelimiter}android".lowercaseFirst()
                )
                sourceSet.mlModels.replace(
                    "src/${sourceSet.name}/mlModels",
                    "${resourcesPrefixPart}MlModels${layout.targetDelimiter}android".lowercaseFirst()
                )
            }

            else -> Unit
        }

    private fun Project.applyGoogleServicesPlugin() {
        if (file("google-services.json").exists()) {
            pluginManager.apply("com.google.gms.google-services")
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
