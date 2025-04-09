package gradle.plugins.signing.tasks

import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import klib.data.type.serialization.serializer.SerializableAnyMap
import gradle.plugins.signing.SignContentPolymorphicSerializer
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
    override val signs: Set<@Serializable(with = SignContentPolymorphicSerializer::class) Any>? = null,
) : DefaultTask<Sign>(), SignatureSpec<Sign>, Signer {

    context(Project)
    override fun applyTo(receiver: Sign) {
        super<DefaultTask>.applyTo(receiver)
        super<SignatureSpec>.applyTo(receiver)
        applyTo(
            receiver::sign,
            receiver::sign,
            receiver::sign,
            receiver::sign,
            receiver::sign,
        )
    }

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<Sign>())
}
