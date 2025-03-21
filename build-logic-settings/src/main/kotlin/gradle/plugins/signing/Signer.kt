package gradle.plugins.signing

import gradle.accessors.publishing
import gradle.api.getByNameOrAll
import java.io.File
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.publish.Publication
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.gradle.plugins.signing.SignOperation

internal interface Signer {

    /**
     * Digitally signs the files, generating signature files alongside them.
     *
     *
     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
     * The returned [sign operation][SignOperation] gives access to the created signature files.
     *
     * If there is no configured default signatory available, the sign operation will fail.
     *
     * @param files The files to sign.
     * @return The executed [sign operation][SignOperation].
     */
    val sign: Set<@Serializable(with = SignSerializer::class) Any>?

    context(Project)
    fun applyTo(
        signConfigurations: (Array<Configuration>) -> Unit,
        signPublications: (Array<Publication>) -> Unit,
        signArtifacts: (Array<PublishArtifact>) -> Unit,
        signFiles: (Array<File>) -> Unit,
        signClassifierFile: (classifier: String, Array<File>) -> Unit,
    ) {
        sign?.let { sign ->
            val (references, files) = sign.filterIsInstance<String>().partition { value -> value.startsWith("$") }

            references
                .resolveReferences("configurations")
                .flatMap(configurations::getByNameOrAll)
                .toTypedArray()
                .let(signConfigurations)

            references
                .resolveReferences("publications")
                .flatMap(publishing.publications::getByNameOrAll)
                .toTypedArray()
                .let(signPublications)

            val allArtifacts = configurations.flatMap(Configuration::getAllArtifacts)

            references
                .resolveReferences("artifacts")
                .flatMap { name ->
                    if (name.isEmpty()) allArtifacts else allArtifacts.filter { artifact -> artifact.classifier == name }
                }
                .toTypedArray()
                .let(signArtifacts)

            files.map(::file).toTypedArray().let(signFiles)

            sign.filterIsInstance<SignFile>().forEach { (classifier, files) ->
                signClassifierFile(classifier, files.map(::file).toTypedArray())
            }
        }
    }
}

internal object SignSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        when (element) {
            is JsonPrimitive -> String.serializer()
            is JsonObject -> SignFile.serializer()
            else -> throw SerializationException("Unsupported element: $element")
        }
}

context(Project)
private fun List<String>.resolveReferences(name: String): List<String> {
    val reference = "$$name."
    return filter { value -> value.startsWith(reference) }
        .map { reference -> reference.removePrefix(reference) }
}
