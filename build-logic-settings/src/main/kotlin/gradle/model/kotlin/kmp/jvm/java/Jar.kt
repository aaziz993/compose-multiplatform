package gradle.model.kotlin.kmp.jvm.java

import gradle.model.CopySpec
import gradle.model.Zip
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

/**
 * Assembles a JAR archive.
 */
@Serializable
internal abstract class Jar : Zip() {

    /**
     * The character set used to encode the manifest content.
     *
     * @param manifestContentCharset the character set used to encode the manifest content
     * @see .getManifestContentCharset
     * @since 2.14
     */
    abstract var manifestContentCharset: String?

    /**
     * Sets the manifest for this JAR archive.
     *
     * @param manifest The manifest. May be null.
     */
    abstract var manifest: Manifest?

    /**
     * Adds content to this JAR archive's META-INF directory.
     *
     *
     * The given action is executed to configure a `CopySpec`.
     *
     * @param configureAction The action.
     * @return The created `CopySpec`
     * @since 3.5
     */

    abstract val metaInf: CopySpec?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as Jar

        metadataCharset?.let(named::setMetadataCharset)
        manifestContentCharset?.let(named::setManifestContentCharset)
        manifest?.applyTo(named.manifest)
        metaInf?.applyTo(named.metaInf)
    }
}
