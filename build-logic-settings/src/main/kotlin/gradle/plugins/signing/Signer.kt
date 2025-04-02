package gradle.plugins.signing

import gradle.accessors.files
import gradle.get
import java.io.File
import klib.data.type.collection.takeIfNotEmpty
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.publish.Publication
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
    val signs: Set<@Serializable(with = SignContentPolymorphicSerializer::class) Any>?

    context(Project)
    fun applyTo(
        signConfigurations: (Array<Configuration>) -> Unit,
        signPublications: (Array<Publication>) -> Unit,
        signArtifacts: (Array<PublishArtifact>) -> Unit,
        signFiles: (Array<File>) -> Unit,
        signClassifierFile: (classifier: String, Array<File>) -> Unit,
    ) {
        signs?.let { sign ->
            val (references, files) = sign.filterIsInstance<String>().partition { sign -> sign.startsWith("$") }

            references
                .map { reference ->
                    reference.removePrefix("$").split(".").toTypedArray()
                }
                .flatMap { keys ->
                    project.get(*keys).let { it as? List<*> ?: listOf(it) }
                }.let { signs ->
                    signs.filterIsInstance<Configuration>()
                        .takeIfNotEmpty()
                        ?.toTypedArray()
                        ?.let(signConfigurations)

                    signs.filterIsInstance<Publication>()
                        .takeIfNotEmpty()
                        ?.toTypedArray()
                        ?.let(signPublications)

                    signs.filterIsInstance<PublishArtifact>()
                        .takeIfNotEmpty()
                        ?.toTypedArray()
                        ?.let(signArtifacts)
                }


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


