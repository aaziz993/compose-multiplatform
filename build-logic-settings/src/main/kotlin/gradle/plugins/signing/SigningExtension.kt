package gradle.plugins.signing

import com.vanniktech.maven.publish.tasks.WorkaroundSignatureType
import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.accessors.signing
import gradle.act
import gradle.actIfTrue
import gradle.api.configureEach
import gradle.api.toVersion
import gradle.takeIfTrue
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
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

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("signing") {
            project.signing.isRequired = required ?: !project.version.toString().toVersion().isPreRelease
            useGpgCmd?.actIfTrue(project.signing::useGpgCmd)

            useInMemoryPgpKeys?.let { (defaultKeyId, defaultSecretKey, defaultPassword) ->
                project.signing.useInMemoryPgpKeys(defaultKeyId, defaultSecretKey, defaultPassword)
            }

            applyTo(
                project.signing::sign,
                project.signing::sign,
                project.signing::sign,
                project.signing::sign,
                project.signing::sign,
            )

            // TODO: https://youtrack.jetbrains.com/issue/KT-61313/ https://github.com/gradle/gradle/issues/26132
            project.plugins.withId(project.settings.libs.plugins.plugin("kotlin.multiplatform").id) {
                project.tasks.withType<Sign>().configureEach { sign ->
                    sign.signatureType = WorkaroundSignatureType(
                        sign.signatureType ?: ArmoredSignatureType(),
                        project.layout.buildDirectory.dir("signatures/${sign.name}"),
                    )
                }
            }
        }
}
