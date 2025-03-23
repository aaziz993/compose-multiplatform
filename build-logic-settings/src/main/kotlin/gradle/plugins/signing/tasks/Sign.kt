package gradle.plugins.signing.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.collection.SerializableAnyMap
import gradle.plugins.signing.SignSerializer
import gradle.plugins.signing.Signer
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign

@Serializable
internal data class Sign(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
    override val signatory: Signatory? = null,
    override val signatureType: SignatureType? = null,
    override val required: Boolean? = null,
   override val sign: Set<@Serializable(with = SignSerializer::class) Any>? = null,
) : DefaultTask<Sign>(), SignatureSpec<Sign>, Signer {

    context(Project)
    override fun applyTo(recipient: Sign) {
        super<DefaultTask>.applyTo(recipient)
        super<SignatureSpec>.applyTo(recipient)
        applyTo(
            recipient::sign,
            recipient::sign,
            recipient::sign,
            recipient::sign,
            recipient::sign,
        )
    }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<Sign>())
}
