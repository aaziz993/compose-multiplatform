package gradle.plugins.android

import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.ProductFlavor
import com.android.build.api.variant.Variant
import gradle.api.configureEach
import gradle.api.file.replace
import gradle.api.project.ProjectLayout
import gradle.api.project.android
import gradle.api.project.androidComponents
import gradle.api.project.projectScript
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import klib.data.type.collections.combinatorics.cartesianProduct
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.trySetSystemProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

private val ANDROID_APPLICATION_COMPILATIONS = listOf(
    "testFixtures",
    KotlinCompilation.TEST_COMPILATION_NAME,
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
            is ProjectLayout.Flat -> {
                val variants = mutableSetOf<Variant>()
                androidComponents {
                    onVariants { variant ->
                        variants.add(variant)
                    }
                }


                android.sourceSets.configureEach { sourceSet ->
                    val (srcPart, resourcesPart) =
                        if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                        else {
                            val rest = sourceSet.name.removePrefix("android").lowercaseFirstChar()

                            "" to ""
                        }

                    //compilation-product-flavor><Build-Type>
                    val targetPart = if (sourceSet.name.startsWith("android") ||
                        sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "${layout.targetDelimiter}android"
                    else ""

                    sourceSet.kotlin.replace(
                        "src/${sourceSet.name}/kotlin",
                        "$srcPart$targetPart",
                    )
                    sourceSet.resources.replace(
                        "src/${sourceSet.name}/resources",
                        "${resourcesPart}Resources$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPart$targetPart")
                    sourceSet.manifest.srcFile("$srcPart$targetPart/AndroidManifest.xml")
                    sourceSet.res.replace(
                        "src/${sourceSet.name}/res",
                        "${resourcesPart}Res$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.assets.replace(
                        "src/${sourceSet.name}/assets",
                        "${resourcesPart}Assets$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.aidl.replace(
                        "src/${sourceSet.name}/aidl",
                        "${resourcesPart}Aidl$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.renderscript.replace(
                        "src/${sourceSet.name}/rs",
                        "${resourcesPart}Rs$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.jniLibs.replace(
                        "src/${sourceSet.name}/jniLibs",
                        "${resourcesPart}JniLibs$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.shaders.replace(
                        "src/${sourceSet.name}/shaders",
                        "${resourcesPart}Shaders$targetPart".lowercaseFirstChar(),
                    )
                    sourceSet.mlModels.replace(
                        "src/${sourceSet.name}/mlModels",
                        "${resourcesPart}MlModels$targetPart".lowercaseFirstChar(),
                    )
                }
            }

            else -> Unit
        }

    private fun Project.progressiveVariants(): Sequence<List<String>> = sequence {
        val compilations = listOf(KotlinCompilation.TEST_COMPILATION_NAME)
        val productFlavors = android.productFlavors.map(ProductFlavor::getName)
        val buildTypes = android.buildTypes.map(BuildType::getName)

        yieldAll(buildTypes.map(::listOf))

        yieldAll(productFlavors.cartesianProduct(buildTypes))

        yieldAll(compilations.cartesianProduct(productFlavors, buildTypes))
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
