package gradle.plugins.signing

import com.vanniktech.maven.publish.tasks.WorkaroundSignatureType
import gradle.accessors.publishing
import gradle.accessors.signing
import gradle.api.configureEach
import gradle.api.getByNameOrAll
import gradle.api.toVersion
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.internal.impldep.kotlinx.serialization.json.JsonContentPolymorphicSerializer
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SignOperation
import org.gradle.plugins.signing.type.pgp.ArmoredSignatureType

/**
 * The global signing configuration for a project.
 */
internal abstract class SigningExtension : Signer {

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
    abstract val required: Boolean?

    /**
     * Use GnuPG agent to perform signing work.
     *
     * @since 4.5
     */
    abstract val useGpgCmd: Boolean?

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
    abstract val useInMemoryPgpKeys: InMemoryPgpKeys?

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
    abstract val signClassifierFiles: List<SignFile>?

    context(Project)
    fun applyTo() =
        pluginManager.withPlugin("signing") {
            signing.isRequired = required ?: !version.toString().toVersion().isPreRelease
            useGpgCmd?.takeIf { it }?.run { signing.useGpgCmd() }

            useInMemoryPgpKeys?.let { (defaultKeyId, defaultSecretKey, defaultPassword) ->
                signing.useInMemoryPgpKeys(defaultKeyId, defaultSecretKey, defaultPassword)
            }

            applyTo(
                signing::sign,
                signing::sign,
                signing::sign,
                signing::sign,
                signing::sign,
            )

            // TODO: https://youtrack.jetbrains.com/issue/KT-61313/ https://github.com/gradle/gradle/issues/26132
            plugins.withId("org.jetbrains.kotlin.multiplatform") {
                tasks.withType<Sign>().configureEach { sign ->
                    sign.signatureType = WorkaroundSignatureType(
                        sign.signatureType ?: ArmoredSignatureType(),
                        layout.buildDirectory.dir("signatures/${sign.name}"),
                    )
                }
            }
        }
}
