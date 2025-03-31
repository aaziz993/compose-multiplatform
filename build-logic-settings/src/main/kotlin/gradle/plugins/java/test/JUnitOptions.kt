package gradle.plugins.java.test

import gradle.api.tasks.test.TestFrameworkOptions
import gradle.reflect.trySet
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project

/**
 * The JUnit specific test options.
 */
@Serializable
internal data class JUnitOptions(
    /**
     * The set of categories to run.
     */
    val includeCategories: Set<String>? = null,
    val setIncludeCategories: Set<String>? = null,
    /**
     * The set of categories to exclude.
     */
    val excludeCategories: Set<String>? = null,
    val setExcludeCategories: Set<String>? = null,
) : TestFrameworkOptions<org.gradle.api.tasks.testing.junit.JUnitOptions>() {

    context(Project)
    override fun applyTo(receiver: org.gradle.api.tasks.testing.junit.JUnitOptions) {
        receiver::includeCategories trySet includeCategories
        receiver::setIncludeCategories trySet setIncludeCategories
        receiver::excludeCategories trySet excludeCategories
        receiver::setExcludeCategories trySet setExcludeCategories
    }
}

internal object JUnitContentPolymorphicSerializer :
    JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) Boolean.serializer() else JUnitOptions.serializer()
}
