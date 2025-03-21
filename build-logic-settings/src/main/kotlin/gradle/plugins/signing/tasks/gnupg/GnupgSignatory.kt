package gradle.plugins.signing.tasks.gnupg

import gradle.plugins.signing.tasks.Signatory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.security.internal.gnupg.GnupgSignatory

@Serializable
@SerialName("gnupg")
internal data class GnupgSignatory(
    val name: String? = null,
    val settings: GnupgSettings
) : Signatory {

    context(Project)
    override fun toSignatory(): org.gradle.plugins.signing.signatory.Signatory =
        GnupgSignatory(project, name, settings.toGnupgSettings())
}
