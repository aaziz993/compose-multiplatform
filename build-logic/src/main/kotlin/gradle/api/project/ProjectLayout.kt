package gradle.api.project

import klib.data.type.pair
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.case.splitToWords
import klib.data.type.serialization.serializers.transform.ReflectionMapTransformingPolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

@Serializable(with = ReflectionProjectLayoutMapTransformingPolymorphicSerializer::class)
public sealed class ProjectLayout {

    @Serializable
    @SerialName("default")
    public object Default : ProjectLayout()

    @Serializable
    @SerialName("flat")
    public data class Flat(
        val targetDelimiter: String = "@",
        val androidAllVariantsDelimiter: String = "+",
        val androidVariantDelimiter: String = ""
    ) : ProjectLayout(){
        internal fun androidParts(
            compilations: List<String>,
            compilationName: String,
        ): Pair<String, String> =
            if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
            else (compilations.find { androidCompilationName ->
                compilationName.startsWith(androidCompilationName)
            }?.let { androidCompilationName ->
                "$androidCompilationName${
                    compilationName.removePrefix(androidCompilationName)
                        .splitToWords()
                        .joinToString(androidVariantDelimiter)
                        .addPrefixIfNotEmpty(androidAllVariantsDelimiter)
                }"
            } ?: compilationName.splitToWords()
                .let { words ->
                    words.firstOrNull()
                        .orEmpty() +
                        words.drop(1)
                            .joinToString(androidVariantDelimiter)
                            .addPrefixIfNotEmpty(androidAllVariantsDelimiter)
                }).pair()
    }
}

private object ReflectionProjectLayoutMapTransformingPolymorphicSerializer
    : ReflectionMapTransformingPolymorphicSerializer<ProjectLayout>(
    ProjectLayout::class,
)



