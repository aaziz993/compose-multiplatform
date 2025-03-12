package gradle.publish

import gradle.accessors.publishing
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.artifacts
import org.gradle.plugins.signing.SignOperation
import org.gradle.plugins.signing.SigningExtension

/**
 * The global signing configuration for a project.
 */
@Serializable
public data class SigningExtension(
    /**
     * Whether this task should fail if no signatory or signature type are configured at generation time.
     *
     * If `required` is a [Callable], it will be stored and "called" on demand (i.e. when [.isRequired] is called) and the return value will be interpreting according to the Groovy
     * Truth. For example:
     *
     * <pre>
     * signing {
     * required = { gradle.taskGraph.hasTask("publish") }
     * }
    </pre> *
     *
     * Because the task graph is not known until Gradle starts executing, we must use defer the decision. We can do this via using a [Closure] (which is a [Callable]).
     *
     * For any other type, the value will be stored and evaluated on demand according to the Groovy Truth.
     *
     * <pre>
     * signing {
     * required = false
     * }
    </pre> *
     */
    val required: Boolean? = null,
    /**
     * Use GnuPG agent to perform signing work.
     *
     * @since 4.5
     */
    val useGpgCmd: Boolean? = null,
    /**
     * Use the supplied ascii-armored in-memory PGP secret key and password
     * instead of reading it from a keyring.
     * In case a signing subkey is provided, keyId must be provided as well.
     *
     * <pre>`
     * signing {
     * def keyId = findProperty("keyId")
     * def secretKey = findProperty("mySigningKey")
     * def password = findProperty("mySigningPassword")
     * useInMemoryPgpKeys(keyId, secretKey, password)
     * }
    `</pre> *
     *
     * @since 6.0
     */
    val useInMemoryPgpKeys: InMemoryPgpKeys? = null,
    /**
     * Creates signing tasks that depend on and sign the "archive" produced by the given tasks.
     *
     *
     * The created tasks will be named "sign*&lt;input task name capitalized&gt;*". That is, given a task with the name "jar" the created task will be named "signJar".
     *
     * If the task is not
     * an [org.gradle.api.tasks.bundling.AbstractArchiveTask], an [InvalidUserDataException] will be thrown.
     *
     * The signature artifact for the created task is added to the [ ][.getConfiguration].
     *
     * @param tasks The tasks whose archives are to be signed
     * @return the created tasks.
     */
    val signTasks: List<String>? = null,
    /**
     * Creates signing tasks that sign [all artifacts][Configuration.getAllArtifacts] of the given configurations.
     *
     *
     * The created tasks will be named "sign*&lt;configuration name capitalized&gt;*". That is, given a configuration with the name "conf" the created task will be named "signConf".
     *
     * The signature artifacts for the created tasks are added to the [configuration][.getConfiguration] for this settings object.
     *
     * @param configurations The configurations whose archives are to be signed
     * @return the created tasks.
     */
    val signConfigurations: List<String>? = null,
    /**
     * Creates signing tasks that sign all publishable artifacts of the given publications.
     *
     *
     * The created tasks will be named "sign*&lt;publication name capitalized&gt;*Publication".
     * That is, given a publication with the name "mavenJava" the created task will be named "signMavenJavaPublication".
     *
     * The signature artifacts for the created tasks are added to the publishable artifacts of the given publications.
     *
     * @param publications The publications whose artifacts are to be signed
     * @return the created tasks.
     * @since 4.8
     */
    val signPublications: List<String>? = null,
    /**
     * Digitally signs the publish artifacts, generating signature files alongside them.
     *
     *
     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
     * The returned [sign operation][SignOperation] gives access to the created signature files.
     *
     * If there is no configured default signatory available, the sign operation will fail.
     *
     * @param publishArtifacts The publish artifacts to sign
     * @return The executed [sign operation][SignOperation]
     */
    val signPublishArtifacts: List<String>? = null,
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
    val signFiles: List<String>? = null,
    /**
     * Digitally signs the files, generating signature files alongside them.
     *
     *
     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
     * The returned [sign][SignOperation] gives access to the created signature files.
     *
     * If there is no configured default signatory available, the sign operation will fail.
     *
     * @param classifier The classifier to assign to the created signature artifacts.
     * @param files The publish artifacts to sign.
     * @return The executed [sign operation][SignOperation].
     */
    val signClassifierFile: List<ClassifierFile>? = null,
) {

    context(Project)
    fun applyTo(extension: SigningExtension) {
        required?.let(extension::setRequired)
        useGpgCmd?.takeIf { it }?.run { extension.useGpgCmd() }

        useInMemoryPgpKeys?.let(extension::useInMemoryPgpKeys)

        signTasks?.map(tasks::getByName)?.let { tasks ->
            extension.sign(*tasks.toTypedArray())
        }

        signConfigurations?.map(configurations::getByName)?.let { configurations ->
            extension.sign(*configurations.toTypedArray())
        }

        signPublications?.map(publishing.publications::getByName)?.let { publications ->
            extension.sign(*signPublications.toTypedArray())
        }

        val publishingArtifacts = publishing.publications
            .filterIsInstance<MavenPublication>()
            .flatMap { it.artifacts }

        signPublishArtifacts?.map { name ->
            publishingArtifacts.find { artifact -> artifact.file.name == name }!!
        }?.let { artifacts ->
            extension.sign(*artifacts.toTypedArray())
        }

        signFiles?.map(::file)?.let { files ->
            extension.sign(*files.toTypedArray())
        }

        signClassifierFile?.forEach { (classifier, files) ->
            extension.sign(classifier, *files.map(::file).toTypedArray())
        }
    }
}

@Serializable
internal data class ClassifierFile(
    val classifier: String,
    val files: List<String>
)

@Serializable
internal data class InMemoryPgpKeys(
    val defaultKeyId: String? = null,
    val defaultSecretKey: String? = null,
    val defaultPassword: String? = null
)
