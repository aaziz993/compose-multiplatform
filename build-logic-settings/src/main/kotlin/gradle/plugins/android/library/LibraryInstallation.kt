package gradle.plugins.android.library

import com.android.build.api.dsl.LibraryInstallation
import gradle.plugins.android.Installation
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring installation options for Library plugins.
 *
 * This is accessed via [LibraryExtension.installation]
 */
@Serializable
internal data class LibraryInstallation(
    override val timeOutInMs: Int? = null,
    override val installOptions: List<String>? = null,
    override val setInstallOptions: List<String>? = null,
) : Installation<LibraryInstallation>
