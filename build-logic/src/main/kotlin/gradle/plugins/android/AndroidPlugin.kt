package gradle.plugins.android

import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.androidApplication
import gradle.api.project.projectScript
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.case.splitToWords
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.trySetSystemProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

private val ANDROID_APPLICATION_COMPILATIONS = listOf(
    "androidTest",
    "testFixtures",
    SourceSet.TEST_SOURCE_SET_NAME,
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
        when (val layout = projectScript.layout) {
            is ProjectLayout.Flat -> androidApplication.sourceSets.configureEach { sourceSet ->
                val (srcPart, resourcesPart) =
                    if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                    else (ANDROID_APPLICATION_COMPILATIONS.find { compilationName ->
                        sourceSet.name.startsWith(compilationName)
                    }?.let { compilationName ->
                        "$compilationName${
                            sourceSet.name.removePrefix(compilationName)
                                .splitToWords()
                                .joinToString(layout.androidVariantDelimiter)
                                .addPrefixIfNotEmpty(layout.androidAllVariantsDelimiter)
                        }"
                    } ?: sourceSet.name).pair()

                sourceSet.kotlin.replace(
                    "src/${sourceSet.name}/kotlin",
                    "$srcPart${layout.targetDelimiter}android",
                )
                sourceSet.resources.replace(
                    "src/${sourceSet.name}/resources",
                    "${resourcesPart}Resources${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPart${layout.targetDelimiter}android")
                sourceSet.manifest.srcFile("$srcPart${layout.targetDelimiter}android/AndroidManifest.xml")
                sourceSet.res.replace(
                    "src/${sourceSet.name}/res",
                    "${resourcesPart}Res${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.assets.replace(
                    "src/${sourceSet.name}/assets",
                    "${resourcesPart}Assets${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.aidl.replace(
                    "src/${sourceSet.name}/aidl",
                    "${resourcesPart}Aidl${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.renderscript.replace(
                    "src/${sourceSet.name}/rs",
                    "${resourcesPart}Rs${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.jniLibs.replace(
                    "src/${sourceSet.name}/jniLibs",
                    "${resourcesPart}JniLibs${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.shaders.replace(
                    "src/${sourceSet.name}/shaders",
                    "${resourcesPart}Shaders${layout.targetDelimiter}android".lowercaseFirstChar(),
                )
                sourceSet.mlModels.replace(
                    "src/${sourceSet.name}/mlModels",
                    "${resourcesPart}MlModels${layout.targetDelimiter}android".lowercaseFirstChar(),
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
