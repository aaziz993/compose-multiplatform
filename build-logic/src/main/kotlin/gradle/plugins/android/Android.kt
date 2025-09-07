package gradle.plugins.android

import com.android.build.gradle.BaseExtension
import gradle.api.configureEach
import gradle.api.file.replace
import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.decapitalize
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

private val TEST_SOURCE_SET_NAME_PREFIXES = listOf(
    SourceSet.TEST_SOURCE_SET_NAME,
    "androidTest",
    "testFixtures",
)

context(project: Project)
public fun <T:BaseExtension> T.flatten(
    targetDelimiter: String = "@",
    androidAllVariantsDelimiter: String = "+",
    androidVariantDelimiter: String = ""
) {
    if (project.pluginManager.hasPlugin("com.android.library")
        || project.pluginManager.hasPlugin("com.android.application")
    ) {
        sourceSets.configureEach { sourceSet ->
            val (srcPrefixPart, resourcesPrefixPart) =
                if (sourceSet.name == SourceSet.MAIN_SOURCE_SET_NAME) "src" to ""
                else (TEST_SOURCE_SET_NAME_PREFIXES.find { prefix ->
                    sourceSet.name.startsWith(prefix)
                }?.let { prefix ->
                    "$prefix${
                        sourceSet.name.removePrefix(prefix).addPrefixIfNotEmpty(androidAllVariantsDelimiter)
                    }"
                } ?: sourceSet.name).pair()

            sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPrefixPart${targetDelimiter}android")
            sourceSet.resources.replace(
                "src/${sourceSet.name}/resources",
                "${resourcesPrefixPart}Resources${targetDelimiter}android".decapitalize()
            )
            sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart${targetDelimiter}android")
            sourceSet.manifest.srcFile("$srcPrefixPart${targetDelimiter}android/AndroidManifest.xml")
            sourceSet.res.replace(
                "src/${sourceSet.name}/res",
                "${resourcesPrefixPart}Res${targetDelimiter}android".decapitalize()
            )
            sourceSet.assets.replace(
                "src/${sourceSet.name}/assets",
                "${resourcesPrefixPart}Assets${targetDelimiter}android".decapitalize()
            )
            sourceSet.aidl.replace(
                "src/${sourceSet.name}/aidl",
                "${resourcesPrefixPart}Aidl${targetDelimiter}android".decapitalize()
            )
            sourceSet.renderscript.replace(
                "src/${sourceSet.name}/rs",
                "${resourcesPrefixPart}Rs${targetDelimiter}android".decapitalize()
            )
            sourceSet.jniLibs.replace(
                "src/${sourceSet.name}/jniLibs",
                "${resourcesPrefixPart}JniLibs${targetDelimiter}android".decapitalize()
            )
            sourceSet.shaders.replace(
                "src/${sourceSet.name}/shaders",
                "${resourcesPrefixPart}Shaders${targetDelimiter}android".decapitalize()
            )
            sourceSet.mlModels.replace(
                "src/${sourceSet.name}/mlModels",
                "${resourcesPrefixPart}MlModels${targetDelimiter}android".decapitalize()
            )
        }
    }
}