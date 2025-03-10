package gradle.model.gradle.publish.publication

import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomMailingListSpec

/**
 * Allows to add mailing lists to a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomMailingList
 */
@Serializable
internal data class MavenPomMailingListSpec(
    /**
     * Creates, configures and adds a mailing list to the publication.
     */
    val mailingList: MavenPomMailingList? = null,
) {

    fun applyTo(spec: MavenPomMailingListSpec) {
        mailingList?.let { mailingList ->
            spec.mailingList(mailingList::applyTo)
        }
    }
}
