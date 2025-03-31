package gradle.plugins.signing

import gradle.accessors.files
import gradle.accessors.publishing
import gradle.api.getByNameOrAll
import java.io.File
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
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
    val sign: Set<@Serializable(with = SignContentPolymorphicSerializer::class) Any>?

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

            files.map(project::file).toTypedArray().let(signFiles)

            sign.filterIsInstance<SignFile>().forEach { (classifier, files) ->
                signClassifierFile(classifier, files.map(project::file).toTypedArray())
            }
        }
    }
}

internal object SignContentPolymorphicSerializer : JsonContentPolymorphicSerializer<Any>(Any::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Any> =
        if (element is JsonPrimitive) String.serializer() else SignFile.serializer()
}


