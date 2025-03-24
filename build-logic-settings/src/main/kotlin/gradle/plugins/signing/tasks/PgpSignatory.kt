package gradle.plugins.signing.tasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.plugins.signing.signatory.pgp.PgpSignatoryFactory

@Serializable
@SerialName("pgp")
internal data class PgpSignatory(
    val name: String? = null,
    val keyId: String,
    val keyRing: String,
    val password: String,
) : Signatory {

    context(project: Project)
    override fun toSignatory(): org.gradle.plugins.signing.signatory.Signatory =
        PgpSignatoryFactory().createSignatory(
            name,
            keyId,
            file(keyRing),
            password,
        )
}
