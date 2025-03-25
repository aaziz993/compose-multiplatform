package gradle.api.publish.maven

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.publish.maven.MavenPomMailingList

/**
 * A mailing list of a Maven publication.
 *
 * @since 4.8
 * @see MavenPom
 *
 * @see MavenPomMailingListSpec
 */
@Serializable
internal data class MavenPomMailingList(
    /**
     * The name of this mailing list.
     */
    val name: String? = null,

    /**
     * The email address or link that can be used to subscribe to this mailing list.
     */
    val subscribe: String? = null,

    /**
     * The email address or link that can be used to subscribe to this mailing list.
     */
    val unsubscribe: String? = null,

    /**
     * The email address or link that can be used to post to this mailing list.
     */
    val post: String? = null,

    /**
     * The URL where you can browse the archive of this mailing list.
     */
    val archive: String? = null,

    /**
     * The alternate URLs where you can browse the archive of this mailing list.
     */
    val otherArchives: Set<String>? = null,
) {

    fun applyTo(receiver: MavenPomMailingList) {
        receiver.name tryAssign name
        receiver.subscribe tryAssign subscribe
        receiver.unsubscribe tryAssign unsubscribe
        receiver.post tryAssign post
        receiver.archive tryAssign archive
        receiver.otherArchives tryAssign otherArchives
    }
}
