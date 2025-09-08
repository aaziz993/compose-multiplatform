package gradle.plugins.android

import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.androidApplication
import gradle.api.project.projectProperties
import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.trySetSystemProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import klib.data.type.primitives.string.case.splitToWords

private val TEST_SOURCE_SET_NAME_PREFIXES = listOf(
    SourceSet.TEST_SOURCE_SET_NAME,
    "androidTest",
    "testFixtures",
)

public class AndroidPlugin : Plugin<Project> {

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
            is ProjectLayout.Flat -> androidApplication.sourceSets.configureEach { sourceSet ->
                val (srcPrefixPart, resourcesPrefixPart) =
                    if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                    else (TEST_SOURCE_SET_NAME_PREFIXES.find { prefix ->
                        sourceSet.name.startsWith(prefix)
                    }?.let { prefix ->
                        "$prefix${
                            sourceSet.name.removePrefix(prefix)
                                .splitToWords()
                                .joinToString(layout.androidVariantDelimiter)
                                .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                        }"
                    } ?: sourceSet.name).pair()

                sourceSet.kotlin.replace(
                    "src/${sourceSet.name}/kotlin",
                    "$srcPrefixPart${layout.targetDelimiter}android",
                )
                sourceSet.resources.replace(
                    "src/${sourceSet.name}/resources",
                    "${resourcesPrefixPart}Resources${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart${layout.targetDelimiter}android")
                sourceSet.manifest.srcFile("$srcPrefixPart${layout.targetDelimiter}android/AndroidManifest.xml")
                sourceSet.res.replace(
                    "src/${sourceSet.name}/res",
                    "${resourcesPrefixPart}Res${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.assets.replace(
                    "src/${sourceSet.name}/assets",
                    "${resourcesPrefixPart}Assets${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.aidl.replace(
                    "src/${sourceSet.name}/aidl",
                    "${resourcesPrefixPart}Aidl${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.renderscript.replace(
                    "src/${sourceSet.name}/rs",
                    "${resourcesPrefixPart}Rs${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.jniLibs.replace(
                    "src/${sourceSet.name}/jniLibs",
                    "${resourcesPrefixPart}JniLibs${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.shaders.replace(
                    "src/${sourceSet.name}/shaders",
                    "${resourcesPrefixPart}Shaders${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.mlModels.replace(
                    "src/${sourceSet.name}/mlModels",
                    "${resourcesPrefixPart}MlModels${layout.targetDelimiter}android".lowercaseFirstChar(),
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
