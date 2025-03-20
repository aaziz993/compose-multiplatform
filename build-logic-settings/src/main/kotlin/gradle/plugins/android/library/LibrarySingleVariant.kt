package gradle.plugins.android.library

import com.android.build.api.dsl.LibrarySingleVariant
import gradle.plugins.android.SingleVariant
import kotlinx.serialization.Serializable

/**
 * Single variant publishing options for library projects.
 */
@Serializable
internal data class LibrarySingleVariant(
    override val variantName: String,
    override val withSourcesJar: Boolean? = null,
    override val withJavadocJar: Boolean? = null,
) : SingleVariant<LibrarySingleVariant>
