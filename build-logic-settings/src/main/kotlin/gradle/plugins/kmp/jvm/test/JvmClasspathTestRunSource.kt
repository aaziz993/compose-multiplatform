package gradle.plugins.kmp.jvm.test

import gradle.plugins.kmp.jvm.test.SingleJvmCompilationTestRunSource
import gradle.plugins.kotlin.KotlinExecution
import gradle.serialization.serializer.JsonPolymorphicSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * A [KotlinExecution.ExecutionSource] that provides the [classpath] and [testClassesDirs] where JVM test classes can be found.
 */
@Serializable(with = JvmClasspathTestRunSourceSerializer::class)
internal interface JvmClasspathTestRunSource : KotlinExecution.ExecutionSource

internal object JvmClasspathTestRunSourceSerializer : JsonPolymorphicSerializer<JvmClasspathTestRunSource>(
    JvmClasspathTestRunSource::class,
) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<JvmClasspathTestRunSource> =
        when {
            element.jsonObject.containsKey("compilation") -> SingleJvmCompilationTestRunSource.serializer()

            element.jsonObject.containsKey("classpath") &&
                element.jsonObject.containsKey("testClassesDirs") -> ClasspathOnlyTestRunSource.serializer()

            element.jsonObject.containsKey("classpathCompilations") &&
                element.jsonObject.containsKey("testCompilations") -> JvmCompilationsTestRunSource.serializer()

            else -> throw IllegalArgumentException("Unknown json value: $element")
        }
}
